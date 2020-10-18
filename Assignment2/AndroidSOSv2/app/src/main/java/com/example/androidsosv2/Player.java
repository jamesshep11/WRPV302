package com.example.androidsosv2;

import java.util.HashMap;
import java.util.Map;

import PubSubBroker.Broker;
import PubSubBroker.Subscriber;

public class Player {

    private Broker broker = Broker.getInstance();

    private String title;
    private int score;              // Player's score
    private int oCount;             // Number of O's placed
    private int sCount;             // Number of S's placed
    private int maxScorePerTurn;    // Max number of points scored in one turn

    public Player(String title) {
        this.title = title;
        setScore(0);
        setoCount(0);
        setsCount(0);
        setMaxScorePerTurn(0);

        loadSubscriber();
    }

    //region Getters & Setters

    public int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;

        Map<String, Object> params = new HashMap<>();
        params.put("score", Integer.toString(score));
        broker.publish(this, "updateScore", params);
    }

    private void setoCount(int oCount) {
        this.oCount = oCount;

        Map<String, Object> params = new HashMap<>();
        params.put("oCount", Integer.toString(oCount));
        params.put("turnCount", Integer.toString(oCount + sCount));
        broker.publish(this, "updateOCount", params);
    }

    private void setsCount(int sCount) {
        this.sCount = sCount;

        Map<String, Object> params = new HashMap<>();
        params.put("sCount", Integer.toString(sCount));
        params.put("turnCount", Integer.toString(oCount + sCount));
        broker.publish(this, "updateSCount", params);
    }

    private void setMaxScorePerTurn(int maxScorePerTurn) {
        this.maxScorePerTurn = maxScorePerTurn;

        Map<String, Object> params = new HashMap<>();
        params.put("max", Integer.toString(maxScorePerTurn));
        broker.publish(this, "updateMax", params);
    }

    //endregion

    private void loadSubscriber(){
        Subscriber playerScored = (publisher, topic, params) -> {
            int points = (int) params.get("score");
            if (publisher.toString().equals(title)) {
                setScore(score + points);
                if (points > maxScorePerTurn)
                    setMaxScorePerTurn(points);
            }
        };
        Subscriber turnOver = (publisher, topic, params) -> {
            if (params.get("player").equals(title))
                if (params.get("letter").equals("S"))
                    setsCount(sCount+1);
                else
                    setoCount(oCount+1);
        };

        broker.subscribe(playerScored, "playerScored");
        broker.subscribe(turnOver, "turnOver");
    }

    @Override
    public String toString() {
        return title;
    }
}
