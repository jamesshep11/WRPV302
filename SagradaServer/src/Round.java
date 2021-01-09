import com.example.segrada.Die.Die;

public class Round {
    static private Round instance;

    static private Turn turn;
    private Die draftPool;
    private int turnCount;

    private Round(){
        draftPool = new Die();
        turnCount = 0;
    }

    static public Round getInstance(){
        if (instance == null) {
            instance = new Round();
            turn = Turn.getInstance();
        }

        return instance;
    }

    public void nextRound(Die bag){
        turnCount = 0;
        turn.nextPlayer();

        // Pick die to make the draft pool
        draftPool.clear();
        bag.shuffle();
        for (int i = 0; i < 9; i++)
            draftPool.add(bag.remove(i));

        // Roll the die
        draftPool.roll();

        // Sort the die
        draftPool.sort();
    }

    public Die getDraftPool(){
        return draftPool;
    }

    public void nextTurn(){
        turnCount++;
        if (turnCount < 5)
            turn.nextPlayer();
        else if (turnCount > 5)
            turn.prevPlayer();
    }

    public int getPlayer(){
        return turn.getPlayer();
    }

    public int getTurnCount() {
        return turnCount;
    }
}
