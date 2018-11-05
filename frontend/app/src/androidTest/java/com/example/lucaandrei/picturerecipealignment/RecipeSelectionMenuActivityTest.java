package com.example.lucaandrei.picturerecipealignment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.example.lucaandrei.picturerecipealignment.tabs.IngredientsFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;



@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeSelectionMenuActivityTest {

    private String mStringToBetyped;

    @Rule
    public FragmentTestRule<?, IngredientsFragment> mActivityRule =
            FragmentTestRule.create(IngredientsFragment.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }

    @Test
    public void add_ingredient_button_resets_input_field() {
        // Type text
        onView(withId(R.id.ingredient_input))
                .perform(typeText(mStringToBetyped), closeSoftKeyboard());

        // Press the button
        onView(withId(R.id.add_ingredient)).perform(click());

        // Check that the text has changed
        onView(withId(R.id.ingredient_input))
                .check(matches(withText("")));
    }

    @Test
    public void add_ingredient_button_adds_ingredient_to_list() {
        // Type text
        onView(withId(R.id.ingredient_input))
                .perform(typeText(mStringToBetyped), closeSoftKeyboard());

        // Press the button
        onView(withId(R.id.add_ingredient)).perform(click());

        // Check that the item was added to the list
        onView(allOf(withText(mStringToBetyped), withParent(withId(R.id.ingredients_list)))).check(matches(isDisplayed()));
    }

}
