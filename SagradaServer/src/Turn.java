import java.util.Random;

public class Turn {
    private int player;

    public Turn(){
        player = -1; //new Random().nextInt(4);
    }

    public void nextPlayer(){
        // Select the next player to start the round
        if (player == Game.numPlayers-1)
            player = -1;
        player++;
    }

    public void prevPlayer(){
        if (player == 0)
            player = Game.numPlayers;
        player--;
    }

    public int getPlayer() {
        return player;
    }
}
