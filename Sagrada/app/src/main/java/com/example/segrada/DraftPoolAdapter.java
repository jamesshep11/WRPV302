package com.example.segrada;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segrada.Die.Dice;
import com.example.segrada.Die.DiceView;
import com.example.segrada.Die.Die;
import com.example.segrada.PubSubBroker.Broker;

import java.util.HashMap;

public class DraftPoolAdapter extends RecyclerView.Adapter<DraftPoolAdapter.diceViewHolder> {

    private Context context;
    private Die draftPool;
    private Broker broker;
    private boolean clickable = false;

    public DraftPoolAdapter(Context context) {
        this.context = context;
        this.broker = Broker.getInstance();
        this.draftPool = new Die();
    }


    public void setDraftPool(Die draftPool) {
        this.draftPool = draftPool;
    }

    public void setClickable(boolean clickable){
        this.clickable = clickable;
    }

    @NonNull
    @Override
    public diceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dice, parent, false);
        return new diceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull diceViewHolder holder, int position) {

        Dice dice = draftPool.get(position);

        holder.diceView.setDice(dice);

        if (clickable) {
            holder.diceView.setOnClickListener(view -> {
                HashMap<String, Object> params = new HashMap<>();
                params.put("diceView", view);
                broker.publish(this, "diceSelected", params);
            });
        }
    }

    @Override
    public int getItemCount() {
        return draftPool.count();
    }

    public static class diceViewHolder extends RecyclerView.ViewHolder {
        DiceView diceView;

        public diceViewHolder(View parentView) {
            super(parentView);
            diceView = parentView.findViewById(R.id.diceView);
        }
    }
}
