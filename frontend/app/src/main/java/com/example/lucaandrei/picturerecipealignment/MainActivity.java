package com.example.lucaandrei.picturerecipealignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadImageMenuSelection(View view) {
        Intent intent = new Intent(this, ImageSelectionMenuActivity.class);
        startActivity(intent);
    }

    public void loadIngredientsMenuSelection(View view) {
        Intent intent = new Intent(this, RecipeSelectionMenuActivity.class);
        startActivity(intent);
    }
}
