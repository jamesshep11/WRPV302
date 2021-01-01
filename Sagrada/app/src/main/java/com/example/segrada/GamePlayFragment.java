package com.example.segrada;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamePlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamePlayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Active = "Active";
    private static final String GRID = "Grid";

    // TODO: Rename and change types of parameters
    private Grid grid;
    private GridView gridView;
    private boolean active;


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
    public static GamePlayFragment newInstance(boolean active, Grid grid) {
        GamePlayFragment fragment = new GamePlayFragment();
        Bundle args = new Bundle();
        args.putBoolean(Active, active);
        args.putSerializable(GRID, grid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            active = getArguments().getBoolean(Active);
            grid = (Grid)getArguments().getSerializable(GRID);
        }
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

        gridView.connectToUI();
        gridView.loadGrid(grid);
    }
}
