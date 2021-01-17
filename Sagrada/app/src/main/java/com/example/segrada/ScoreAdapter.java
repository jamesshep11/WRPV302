package com.example.segrada;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segrada.Die.Dice;
import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.PubSubBroker.Broker;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.scoreViewHolder> {

    private Context context;
    private ArrayList<Score> scores;

    public ScoreAdapter(Context context, ArrayList<Score> scores) {
        this.context = context;
        this.scores = scores;
    }

    @NonNull
    @Override
    public scoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.score_view, parent, false);
        return new scoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull scoreViewHolder holder, int position) {

        Score score = scores.get(position);

        holder.txtPlayer.setText(context.getString(R.string.Player, position + 1));
        holder.txtPlayerColor.setText(context.getString(R.string.PlayerColor, score.getPlayerColor()));
        holder.txtHoles.setText(context.getString(R.string.Holes, score.getHoles()));
        holder.txtColumns.setText(context.getString(R.string.Columns, score.getColumns()));
        holder.txtPairs56.setText(context.getString(R.string.Pairs56, score.getPair56()));
        holder.txtColorSets.setText(context.getString(R.string.ColorSets, score.getColorSets()));
        holder.txtScore.setText(Integer.toString(score.getTotal()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public static class scoreViewHolder extends RecyclerView.ViewHolder {
        TextView txtPlayer;
        TextView txtPlayerColor;
        TextView txtHoles;
        TextView txtColumns;
        TextView txtPairs56;
        TextView txtColorSets;
        TextView txtScore;

        public scoreViewHolder(View parentView) {
            super(parentView);
            txtPlayer = parentView.findViewById(R.id.txtPlayerScore);
            txtPlayerColor = parentView.findViewById(R.id.txtPlayerColor);
            txtHoles = parentView.findViewById(R.id.txtHoles);
            txtColumns = parentView.findViewById(R.id.txtColumns);
            txtPairs56 = parentView.findViewById(R.id.txtPairs56);
            txtColorSets = parentView.findViewById(R.id.txtColorSet);
            txtScore = parentView.findViewById(R.id.txtScore);
        }
    }
}

