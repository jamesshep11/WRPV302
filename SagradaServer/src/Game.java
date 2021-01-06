import PubSubBroker.Broker;
import com.example.segrada.Grid;

import java.util.*;

public class Game {
    private Broker serverBroker;
    private Broker broker;

    private final String[] colors = {"blue", "yellow", "purple", "red", "green"};
    private ArrayList<Client> clients;
    private ArrayList<Grid> grids;

    public Game(ArrayList<Client> clients, Broker broker, Broker serverBroker) {
        this.clients = clients;
        this.serverBroker = serverBroker;
        this.broker = broker;
        grids = Main.getGrids();

        broker.subscribe("EndGame", (publisher, topic, params) -> endGame());

        // Randomly allocate grids and colors
        Collections.shuffle(grids);
        Collections.shuffle(Arrays.asList(colors));

        startGame();
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

        for(int i = 0; i < clients.size(); i++){
            params.put("player", i+1);
            clients.get(i).sendObject(params);
        }
    }

    private void sendToAll(Map<String, Object> params){
        for (Client client : clients)
            client.sendObject(params);
    }

    private void endGame(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "EndGame");
        sendToAll(params);
        broker.publish(this, "CloseConnection", null);

        serverBroker.publish(this, "EndGame", null);
    }
}
