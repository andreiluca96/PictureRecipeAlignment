package com.example.lucaandrei.picturerecipealignment.result;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lucaandrei.picturerecipealignment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.lucaandrei.picturerecipealignment.cache.ImageCache.clearMemCache;
import static com.example.lucaandrei.picturerecipealignment.cache.ImageCache.getBitmapFromMemCache;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final ListView list = findViewById(R.id.result_ingredients);

        Intent intent = getIntent();
//        byte[] image = intent.getByteArrayExtra("image");
        byte[] image = getBitmapFromMemCache("image");
        String[] ingredientsArray = intent.getStringArrayExtra("ingredients");

        final List<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsArray));
        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        list.setAdapter(itemsAdapter);

        final Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        clearMemCache();
        final ImageView imageView = findViewById(R.id.resultImage);

        imageView.post(() -> imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(),
                imageView.getHeight(), false)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

}
