import PubSubBroker.Broker;
import com.example.segrada.Die.Dice;
import com.example.segrada.Die.Die;
import com.example.segrada.Grids.Grid;
import com.example.segrada.Grids.GridBlock;
import com.example.segrada.Score;

import java.util.*;

public class Game {
    private final Broker serverBroker;
    private final Broker broker;

    static int numPlayers = 4;
    private final String[] colors = {"blue", "yellow", "purple", "red", "green"};
    private final ArrayList<Client> clients;
    private final ArrayList<Grid> grids = Main.getGrids();
    private Die bag = fillBag();
    private Round round;
    private int roundCount = 0;

    public Game(ArrayList<Client> clients, Broker broker, Broker serverBroker) {
        this.clients = clients;
        this.serverBroker = serverBroker;
        this.broker = broker;
        this.round = new Round();

        subToBroker();

        // Randomly allocate grids and colors
        Collections.shuffle(grids);
        Collections.shuffle(Arrays.asList(colors));
        bag.shuffle();

        startGame();
    }

    private int counter = 0;
    private void subToBroker(){
        broker.subscribe("GameStarted", (publisher, topic, params) -> {
            counter++;
            if (counter == numPlayers) {
                nextRound();
                counter = 0;
            }
        });
        broker.subscribe("RoundStarted", (publisher, topic, params) -> {
            counter++;
            if (counter == numPlayers) {
                nextTurn();
                counter = 0;
            }
        });
        broker.subscribe("CheckSkippable", (publisher, topic, params) -> checkSkippable(params));
        broker.subscribe("GetValidSlots", (publisher, topic, params) -> {
            Grid validSlots = getAvailableSlots(params);
            HashMap<String, Object> newParams = new HashMap<>();
            newParams.put("topic", "ValidSlots");
            newParams.put("validSlots", validSlots);
            clients.get(round.getPlayer()).sendObject(newParams);
        });
        broker.subscribe("DicePlaced", (publisher, topic, params) -> {
            ((Grid)params.get("grid")).setFirstDice(false);

            params.put("player", round.getPlayer());
            sendToAll(params);
        });
        broker.subscribe("EndTurn", (publisher, topic, params) -> {
            counter++;
            if (counter == numPlayers) {
                nextTurn();
                counter = 0;
            }
        });
        broker.subscribe("SkipTurn", (publisher, topic, params) -> nextTurn());
        broker.subscribe("CalcScores", (publisher, topic, params) -> {
            counter++;
            if (counter == numPlayers){
                calcScores(params);
                counter = 0;
            }
        });

        broker.subscribe("connectionLost", (publisher, topic, params) -> {
            counter++;
            if (counter == numPlayers)
                endGame();
        });
    }

    private Die fillBag(){
        Die die = new Die();
        for (String color : colors)
            for (int i = 1; i <= 18; i++)
                die.add(color);

        return die;
    }

    private void startGame(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "StartGame");
        params.put("grid1", grids.get(0));
        params.put("grid2", grids.get(1));
        params.put("grid3", grids.get(2));
        params.put("grid4", grids.get(3));
        params.put("color1", colors[0]);
        params.put("color2", colors[1]);
        params.put("color3", colors[2]);
        params.put("color4", colors[3]);

        sendToAll(params);
    }

    private void nextRound(){
        // Initialize a new round
        roundCount++;
        if (roundCount > 10){
            finishGame();
            return;
        }
        round.nextRound(bag);

        // Notify clients to start round
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "StartRound");
        params.put("draftPool", round.getDraftPool());
        params.put("round", roundCount);
        sendToAll(params);
    }

    private void nextTurn(){
        round.nextTurn();
        if (round.getTurnCount() > 8){
            nextRound();
            return;
        }
        int player = round.getPlayer();

        // Notify clients to start turn
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "StartTurn");
        params.put("player", player);
        sendToAll(params);
    }

    private Grid getAvailableSlots(Map<String, Object> params){
        Grid grid = (Grid) params.get("grid");
        Dice dice = (Dice) params.get("dice");
        return grid.findValid(dice);
    }

    private void checkSkippable(Map<String, Object> params){
        boolean skippable = true;

        Die draftPool = (Die) params.get("draftPool");
        for (int i = 0; i < draftPool.count(); i++) {
            params.put("dice", draftPool.get(i));
            Grid validSlots = getAvailableSlots(params);
            if (validSlots.hasValid())
                skippable = false;
        }

        HashMap<String, Object> newParams = new HashMap<>();
        newParams.put("topic", "Skippable");
        newParams.put("skippable", skippable);
        clients.get(round.getPlayer()).sendObject(newParams);
    }

    private void finishGame(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "FinishGame");
        sendToAll(params);
    }

    private void calcScores(Map<String, Object> params){
        ArrayList<Score> scores = new ArrayList<>();
        ArrayList<Grid> grids = (ArrayList<Grid>) params.get("grids");
        for (int i = 0; i < grids.size(); i++)
            scores.add(grids.get(i).calcScore(colors[i]));

        HashMap<String, Object> newParams = new HashMap<>();
        newParams.put("topic", "ShowScores");
        newParams.put("scores", scores);
        sendToAll(newParams);
    }

    private void endGame(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "CloseConnection");
        sendToAll(params);

        serverBroker.publish(this, "EndGame", null);
    }

    private void sendToAll(Map<String, Object> params){
        for (Client client : clients)
            client.sendObject(params);
    }
}
