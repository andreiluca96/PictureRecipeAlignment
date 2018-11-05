package com.example.lucaandrei.picturerecipealignment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class RecipeSelectionMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_selection_menu);

        // find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // create an adapter that knows which fragment should be shown on each page
        RecipeFragmentPageAdapter adapter = new RecipeFragmentPageAdapter(this, getSupportFragmentManager());

        // set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
}
