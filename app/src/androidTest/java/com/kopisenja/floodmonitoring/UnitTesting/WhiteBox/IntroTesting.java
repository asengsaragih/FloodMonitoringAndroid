package com.kopisenja.floodmonitoring.UnitTesting.WhiteBox;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.activity.IndexActivity;
import com.kopisenja.floodmonitoring.activity.IntroActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class IntroTesting {

    @Rule
    public ActivityTestRule<IntroActivity> activityTestRule = new ActivityTestRule<>(IntroActivity.class);

    @Test
    public void introtest() {
        onView(withId(R.id.button_intro_right)).perform(click());
        onView(withId(R.id.button_intro_left)).perform(click());
        onView(withId(R.id.button_intro_right)).perform(click());
        onView(withId(R.id.button_intro_right)).perform(click());
    }
}
