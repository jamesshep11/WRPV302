package com.example.segrada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.segrada.PubSubBroker.Broker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    Broker broker = Broker.getInstance();

    private RecyclerView recyclerView;
    private ScoreAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        broker.subscribe("loadResults", (publisher, topic, params) -> loadResults(params));
        broker.publish(this, "getResults", null);
    }

    private void loadResults(Map<String, Object> params){
        ArrayList<Score> scores = (ArrayList<Score>) params.get("scores");
        for (int i = 0; i < scores.size(); i++)
            scores.get(i).setPlayerNum(i+1);
        Collections.sort(scores, (score1, score2) -> score1.getTotal() - score2.getTotal());

        recyclerView = findViewById(R.id.rvDraftPool);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ScoreAdapter(this, scores);
        recyclerView.setAdapter(mAdapter);
    }
}
