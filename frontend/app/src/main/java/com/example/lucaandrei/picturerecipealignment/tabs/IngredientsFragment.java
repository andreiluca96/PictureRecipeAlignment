package com.example.lucaandrei.picturerecipealignment.tabs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.example.lucaandrei.picturerecipealignment.result.ResultActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

public class IngredientsFragment extends Fragment {
    private static final String INGREDIENTS_URL = "http://10.0.2.2:5000/ingredients";

    public IngredientsFragment() {
        // Required empty public constructor

    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
        submit.setOnClickListener(view -> {
            try {
                String ingredientsJsonList = ingredients
                        .stream()
                        .reduce((s1, s2) -> s1 + "," + s2)
                        .orElse("");

                String ingredientsJson =
                        "{" +
                                "\"ingredients\": [" + ingredientsJsonList + "]" +
                                "}";

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ingredientsJson);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .post(body)
                        .url(INGREDIENTS_URL)
                        .build();

                final Response[] response = {null};

                Thread thread = new Thread(() -> {
                    try {
                        response[0] = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
                thread.join();

                JSONObject responseBody = new JSONObject(response[0].body().string());

                String imageB64 = responseBody.getString("image");


                Intent myIntent = new Intent(act, ResultActivity.class);
                myIntent.putExtra("image", Base64.getDecoder().decode(imageB64));
                myIntent.putExtra("ingredients", ingredients);
                act.startActivity(myIntent);
                // empty ingredients list
                itemsAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        return rootView;
    }


}
