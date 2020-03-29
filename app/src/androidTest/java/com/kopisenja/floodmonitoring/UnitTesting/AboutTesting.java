package com.kopisenja.floodmonitoring.UnitTesting;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.activity.IndexActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AboutTesting {

    @Rule
    public ActivityTestRule<IndexActivity> activityTestRule = new ActivityTestRule<>(IndexActivity.class);

    @Test
    public void testAbout() throws InterruptedException {
        Thread.sleep(1000);

        try {
            onView(withId(R.id.action_about)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
            onView(withText(R.string.activity_about)).perform(click());
        }

        onView(withId(R.id.imageView_button_right)).perform(click());
        onView(withId(R.id.imageView_button_left)).perform(click());
        onView(withId(R.id.imageView_button_right)).perform(click());
        onView(withId(R.id.imageView_button_left)).perform(click());

        onView(withId(R.id.imageView_about_back)).perform(click());
    }
}
