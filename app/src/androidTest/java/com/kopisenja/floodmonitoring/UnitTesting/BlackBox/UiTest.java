package com.kopisenja.floodmonitoring.UnitTesting.BlackBox;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.activity.IndexActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UiTest {

    @Rule
    public ActivityTestRule<IndexActivity> activityTestRule = new ActivityTestRule<>(IndexActivity.class);

    @Test
    public void allActivityTest() throws InterruptedException {
        Thread.sleep(2000);

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

        Thread.sleep(2000);

        try {
            onView(withId(R.id.action_other_marker)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
            onView(withText("Lokasi Lainnya")).perform(click());
        }

        Thread.sleep(3000);

        onView(withId(R.id.bottom_sheet_detail)).perform(click());
        onView(withId(R.id.bottom_sheet_category)).perform(click());
        onView(withId(R.id.bottom_sheet_dateTime)).perform(click());

        onView(withId(R.id.textView_bottom_sheet_other)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.recycleView_history)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(1000);
    }
}
