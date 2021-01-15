import java.util.Random;

public class Turn {
    private int player;

    public Turn(){
        player = -2; //new Random().nextInt(4);
    }

    public void nextPlayer(){
        // Select the next player to start the round
        if (player == 3)
            player = -1;
        player++;
    }

    public void prevPlayer(){
        if (player == 0)
            player = 4;
        player--;
    }

    public int getPlayer() {
        return player;
    }
}
