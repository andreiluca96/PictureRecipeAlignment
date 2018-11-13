package com.example.lucaandrei.picturerecipealignment.tabs;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lucaandrei.picturerecipealignment.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionsFragment extends Fragment {


    public InstructionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // get instructions list
        final ListView list = (ListView)this.getActivity().findViewById(R.id.instructions_list);

        final ArrayList<String> instructions = new ArrayList<>();
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, instructions);
        list.setAdapter(itemsAdapter);

        final Activity act = this.getActivity();

        Button add = this.getActivity().findViewById(R.id.add_instruction);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the instruction from input field
                EditText addIngredientComponent = act.findViewById(R.id.instruction_input);
                String instruction = addIngredientComponent.getText().toString();

                //add the new instruction to list view
                instructions.add(instruction);

                //reset input
                addIngredientComponent.setText("");

                itemsAdapter.notifyDataSetChanged();
            }
        });

        Button submit = this.getActivity().findViewById(R.id.submit_instructions);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: request with instructions array

                // empty instructions list
                instructions.clear();

                itemsAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        return rootView;
    }

}
