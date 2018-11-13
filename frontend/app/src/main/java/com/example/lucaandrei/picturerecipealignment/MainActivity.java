package com.example.lucaandrei.picturerecipealignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonImageMenuSelectionClick(View view) {
        Intent intent = new Intent(this, ImageSelectionMenuActivity.class);
        startActivity(intent);
    }

    public void onButtonIngredientsMenuSelectionClick(View view) {
        Intent intent = new Intent(this, RecipeSelectionMenuActivity.class);
        startActivity(intent);
    }
}
