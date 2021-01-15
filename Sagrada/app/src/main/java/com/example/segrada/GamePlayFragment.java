package com.example.segrada;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.segrada.Grids.Grid;
import com.example.segrada.Grids.GridView;
import com.example.segrada.PubSubBroker.Broker;

import java.lang.reflect.Field;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamePlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamePlayFragment extends Fragment {
    private Context context;
    private Broker broker;
    private Game game;

    private int fragNum;
    private boolean active;
    private Grid grid;
    private GridView gridView;
    private String color;

    private RecyclerView recyclerView;
    private DraftPoolAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean rvSet = false;


    private GamePlayFragment(Context context) {
        this.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fragNum the fragment number. Corresponds with the player number that this fragment represents.
     * @return A new instance of fragment GamePlayFragment.
     */
    public static GamePlayFragment newInstance(Context context, int fragNum) {
        GamePlayFragment fragment = new GamePlayFragment(context);
        fragment.game = Game.getInstance(null);
        fragment.broker = Broker.getInstance();

        fragment.fragNum = fragNum;
        fragment.grid = fragment.game.getGrids().get(fragNum);
        if (fragment.game.getThisPLayer() == fragNum)
            fragment.color = fragment.game.getColor();
        else
            fragment.color = "white";
        fragment.active = false;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gridView = new GridView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //region Set up recyclerView
        if (!rvSet) {
            recyclerView = getView().findViewById(R.id.rvDraftPool);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);

            mAdapter = new DraftPoolAdapter(context);
            recyclerView.setAdapter(mAdapter);
        }
        //endregion

        renderDraftPool();

        //region Handle Player# header
        TextView txtPlayer = getView().findViewById(R.id.txtPlayer);
        txtPlayer.setText(getString(R.string.Player, fragNum+1));
        int colorResId = getResId(color, R.color.class);
        txtPlayer.setBackgroundColor(getResources().getColor(colorResId));
        //endregion

        gridView.connectToUI();
        gridView.loadGrid(grid);
    }

    private void renderDraftPool(){
        mAdapter.setClickable(active);
        mAdapter.setDraftPool(game.getDraftPool());
    }

    private int getResId(String rec, Class<?> aClass) {
        try {
            Field field = aClass.getDeclaredField(rec);
            return field.getInt(field);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //region Getter & Setters
    public int getFragNum(){
        return fragNum;
    }

    public GridView getGridView() {
        return gridView;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    //endregion
}
