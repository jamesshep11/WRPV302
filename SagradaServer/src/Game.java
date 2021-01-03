import PubSubBroker.Broker;
import com.example.segrada.Grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private Broker serverBroker;
    private Broker broker;

    private ArrayList<Client> clients;
    private ArrayList<Grid> grids;
    private ArrayList<String> colors;


    public Game(ArrayList<Client> clients, Broker broker, Broker serverBroker) {
        this.clients = clients;
        this.serverBroker = serverBroker;
        this.broker = broker;
        grids = Main.getGrids();
        colors = new ArrayList<>(Main.colors.keySet());

        broker.subscribe("EndGame", ((publisher, topic, params) -> endGame()));

        // Randomly allocate grids and colors
        Collections.shuffle(grids);
        Collections.shuffle(colors);

        startGame();
    }

    private void startGame(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "StartGame");
        params.put("grid1", grids.get(0));
        params.put("grid2", grids.get(1));
        params.put("grid3", grids.get(2));
        params.put("grid4", grids.get(3));
        params.put("color1", colors.get(0));
        params.put("color2", colors.get(1));
        params.put("color3", colors.get(2));
        params.put("color4", colors.get(3));

        int i = 1;
        for(Client client : clients){
            params.put("player", i++);
            client.sendObjects(params);
        }
    }

    private void sendToAll(Map<String, Object> params){
        for (Client client : clients)
            client.sendObjects(params);
    }

    private void endGame(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("topic", "EndGame");
        sendToAll(params);
        broker.publish(this, "CloseConnection", null);

        serverBroker.publish(this, "EndGame", null);
    }
}
