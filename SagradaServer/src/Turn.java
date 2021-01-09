import com.example.segrada.Die.Die;

import java.util.Random;

public class Turn {
    static private Turn instance;

    private Die draftPool;
    private int player;

    private Turn(){
        Round round = Round.getInstance();
        draftPool = round.getDraftPool();
        player = -2; //new Random().nextInt(4);
    }

    static public Turn getInstance(){
        if (instance == null)
            instance = new Turn();

        return instance;
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
