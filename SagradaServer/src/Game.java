import PubSubBroker.Broker;

public class Game {
    Client[] clients;
    Broker broker;

    public Game(Client[] clients) {
        this.clients = clients;
        broker = Broker.getInstance();
    }
}
