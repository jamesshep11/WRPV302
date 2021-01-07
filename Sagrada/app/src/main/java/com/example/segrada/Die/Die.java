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
            int val = dice1.color.compareTo(dice2.color);
            if (val == 0)
                val = dice1.value - dice2.value;

            return val;
        });
    }

    public void roll(){
        for (Dice dice : die)
            dice.roll();
    }

    public void clear(){
        die.clear();
    }

    public Dice get(int pos) {
        return die.get(pos);
    }
}
