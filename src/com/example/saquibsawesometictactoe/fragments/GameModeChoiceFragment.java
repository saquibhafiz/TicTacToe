package com.example.saquibsawesometictactoe.fragments;

import com.example.saquibsawesometictactoe.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameModeChoiceFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_game_mode_choice, container);
    }
}
