package com.example.lucaandrei.picturerecipealignment.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucaandrei.picturerecipealignment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionsFragment extends Fragment {


    public InstructionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        return rootView;
    }

}
