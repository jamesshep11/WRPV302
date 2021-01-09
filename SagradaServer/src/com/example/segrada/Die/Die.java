package com.example.segrada.Die;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Die implements Serializable {
    private static final long serialVersionUID = 997L;

    ArrayList<Dice> die;

    public Die(){
        die = new ArrayList<>();
    }

    public void add(String color){
        die.add(new Dice(color));
    }

    public void add(Dice dice){
        die.add(dice);
    }

    public Dice remove(int pos) {
        return die.remove(pos);
    }

    public int count(){
        return die.size();
    }

    public void shuffle(){
        Collections.shuffle(die);
    }

    public void sort() {
        Collections.sort(die, (dice1, dice2) -> {
            int val = dice2.getColor().compareTo(dice1.getColor());
            if (val == 0)
                val = dice1.getValue() - dice2.getValue();

            return val;
        });
    }

    public void roll(){
        for (Dice dice : die)
            dice.roll();
    }

    public Dice get(int pos) {
        return die.get(pos);
    }

    public void clear(){
        die.clear();
    }

    public void setDiceViewVarialbes(int dicePos, float x, float y, float dx, float dy){
        Dice dice = die.get(dicePos);
        dice.setViewVariables(x, y, dx, dy);
    }

    public boolean areRolling(){
        boolean rolling = false;
        for (Dice dice : die)
            if (dice.isRolling())
                rolling = true;

        return rolling;
    }

    public void setRolling(boolean rolling){
        for (Dice dice : die)
            dice.setRolling(rolling);
    }
}
