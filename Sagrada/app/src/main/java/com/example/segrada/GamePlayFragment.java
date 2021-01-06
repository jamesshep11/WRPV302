package com.example.segrada;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.segrada.PubSubBroker.Broker;

import java.lang.reflect.Field;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamePlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamePlayFragment extends Fragment {
    // TODO: Rename and change types of parameters
    private Broker broker;

    private boolean active;
    private Grid grid;
    private GridView gridView;
    private String color;
    private int player;


    public GamePlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamePlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamePlayFragment newInstance(boolean active, Grid grid, String color, int player) {
        GamePlayFragment fragment = new GamePlayFragment();
        fragment.broker = Broker.getInstance();

        fragment.active = active;
        fragment.grid = grid;
        fragment.color = color;
        fragment.player = player;

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

        TextView txtPlayer = getView().findViewById(R.id.txtPlayer);
        txtPlayer.setText(getString(R.string.Player, player));
        int colorResId;
        if (active)
            colorResId = getResId(color, R.color.class);
        else
            colorResId = getResId("white", R.color.class);
        txtPlayer.setBackgroundColor(getResources().getColor(colorResId));

        gridView.connectToUI();
        gridView.loadGrid(grid);
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

    public boolean isActive(){
        return active;
    }
}
