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
import android.widget.Switch;

import com.example.lucaandrei.picturerecipealignment.R;
import com.example.lucaandrei.picturerecipealignment.result.ResultActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static com.example.lucaandrei.picturerecipealignment.cache.ImageCache.addBitmapToMemoryCache;

public class IngredientsFragment extends Fragment {
    private static final String INGREDIENTS_URL = "http://10.0.2.2:5000/ingredients";
    private static final String INGREDIENTS_SSE_URL = "http://10.0.2.2:5000/v1/sse/recipe/";
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

        add.setOnClickListener(view -> {
            //get the ingredient from input field
            EditText addIngredientComponent = act.findViewById(R.id.ingredient_input);
            String ingredient = addIngredientComponent.getText().toString();

            //add the new ingredient to list view
            ingredients.add(ingredient);

            //reset input
            addIngredientComponent.setText("");

            itemsAdapter.notifyDataSetChanged();
        });

        Button submit = this.getActivity().findViewById(R.id.submit_ingredients);
        submit.setOnClickListener(view -> {
            try {
                Switch simpleSwitch = act.findViewById(R.id.use_sse);

                if (simpleSwitch.isActivated()) {
                    String ingredientsJsonList = ingredients
                            .stream()
                            .map(s -> "\"" + s + "\"")
                            .reduce((s1, s2) -> s1 + "," + s2)
                            .orElse("");

                    String ingredientsJson =
                            "{" +
                                    "\"ingredients\": [" + ingredientsJsonList + "]" +
                                    "}";

                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ingredientsJson);

                    OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(30, TimeUnit.SECONDS);
                    client.setReadTimeout(30, TimeUnit.SECONDS);
                    client.setWriteTimeout(30, TimeUnit.SECONDS);
                    Request request = new Request.Builder()
                            .post(body)
                            .url(INGREDIENTS_URL)
                            .build();

                    Response response = client.newCall(request).execute();
                    JSONObject jsonBody = new JSONObject(response.body().string());

                    String base64EncodedImage = jsonBody.getString("image");
                    byte[] decodedImage = Base64.getDecoder().decode(base64EncodedImage);

                    System.out.println(decodedImage.length);

                    addBitmapToMemoryCache("image", decodedImage);

                    Intent myIntent = new Intent(act, ResultActivity.class);
//                myIntent.putExtra("image", decodedImage);
                    myIntent.putExtra("ingredients", ingredients.toArray(new String[0]));
                    act.startActivity(myIntent);
                    // empty ingredients list
                    itemsAdapter.notifyDataSetChanged();
                } else {
                    OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(30, TimeUnit.SECONDS);
                    client.setReadTimeout(30, TimeUnit.SECONDS);
                    client.setWriteTimeout(30, TimeUnit.SECONDS);
                    Request request = new Request.Builder()
                            .get()
                            .url(INGREDIENTS_SSE_URL + ingredients.get(0))
                            .build();

                    Response response = client.newCall(request).execute();
                    JSONObject jsonBody = new JSONObject(response.body().string());

                    String base64EncodedImage = jsonBody.getString("image");
                    byte[] decodedImage = Base64.getDecoder().decode(base64EncodedImage);

                    System.out.println(decodedImage.length);

                    addBitmapToMemoryCache("image", decodedImage);

                    Intent myIntent = new Intent(act, ResultActivity.class);
//                myIntent.putExtra("image", decodedImage);
                    myIntent.putExtra("ingredients", ingredients.toArray(new String[0]));
                    act.startActivity(myIntent);
                    // empty ingredients list
                    itemsAdapter.notifyDataSetChanged();
                }
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
