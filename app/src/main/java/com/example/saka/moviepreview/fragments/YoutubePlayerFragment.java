package com.example.saka.moviepreview.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saka.moviepreview.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubePlayerFragment extends Fragment {


    public YoutubePlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_youtube_player, container, false);
    }

}
