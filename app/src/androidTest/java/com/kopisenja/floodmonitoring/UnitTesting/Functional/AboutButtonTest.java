package com.kopisenja.floodmonitoring.UnitTesting.Functional;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.activity.AboutActivity;
import com.kopisenja.floodmonitoring.activity.IndexActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AboutButtonTest {

    @Rule
    public ActivityTestRule<AboutActivity> activityTestRule = new ActivityTestRule<>(AboutActivity.class);

    @Test
    public void testButtonAbout() {
        onView(withId(R.id.imageView_button_right)).perform(click());
        onView(withId(R.id.imageView_button_left)).perform(click());
        onView(withId(R.id.imageView_button_right)).perform(click());
        onView(withId(R.id.imageView_button_left)).perform(click());

        onView(withId(R.id.imageView_about_back)).perform(click());
    }
}
