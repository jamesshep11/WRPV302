import com.example.segrada.Die.Die;

import java.util.Random;

public class Round {
    static private Round instance;

    private Die draftPool;
    private int nextPlayer;

    private Round(){
        draftPool = new Die();
        nextPlayer = new Random().nextInt(4);
    }

    static public Round getInstance(){
        if (instance == null)
            instance = new Round();

        return instance;
    }

    public void nextRound(Die bag){
        // Select the next player to start the round
        if (nextPlayer == 3)
            nextPlayer = -1;
        nextPlayer++;

        // Pick die to make the draft pool
        draftPool.clear();
        bag.shuffle();
        for (int i = 0; i < 9; i++)
            draftPool.add(bag.remove(i));

        // Roll the die
        draftPool.roll();
    }

    public Die getDraftPool(){
        return draftPool;
    }

    public int getNextPlayer(){
        return nextPlayer;
    }
}
