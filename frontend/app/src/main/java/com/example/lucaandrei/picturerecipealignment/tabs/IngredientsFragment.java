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

import com.example.lucaandrei.picturerecipealignment.R;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment {

    public IngredientsFragment() {
        // Required empty public constructor

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // get ingredients list
        final ListView list = this.getActivity().findViewById(R.id.ingredients_list);

        final ArrayList<String> ingredients = new ArrayList<String>();
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, ingredients);
        list.setAdapter(itemsAdapter);

        Button add = this.getActivity().findViewById(R.id.add_ingredient);

        final Activity act = this.getActivity();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the ingredient from input field
                EditText addIngredientComponent = act.findViewById(R.id.ingredient_input);
                String ingredient = addIngredientComponent.getText().toString();

                //add the new ingredient to list view
                ingredients.add(ingredient);

                //reset input
                addIngredientComponent.setText("");

                itemsAdapter.notifyDataSetChanged();
            }
        });

        Button submit = this.getActivity().findViewById(R.id.submit_ingredients);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String ingredient : ingredients) {
                    System.out.println(ingredient);
                }

                // empty ingredients list
                ingredients.clear();
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        return rootView;
    }


}
