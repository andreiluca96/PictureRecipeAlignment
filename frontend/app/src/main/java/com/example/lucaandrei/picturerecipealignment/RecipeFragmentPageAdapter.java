package com.example.lucaandrei.picturerecipealignment;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lucaandrei.picturerecipealignment.tabs.IngredientsFragment;
import com.example.lucaandrei.picturerecipealignment.tabs.InstructionsFragment;

public class RecipeFragmentPageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public RecipeFragmentPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 1)
            return new InstructionsFragment();
        return new IngredientsFragment();
    }

    // This determines the number of tabs
    @Override
    public int getCount(){
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Ingredients";
            case 1:
                return "Instructions";
            default:
                return null;
        }
    }

}