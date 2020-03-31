package com.kopisenja.floodmonitoring.UnitTesting.Functional;

import android.view.View;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.activity.AboutActivity;
import com.kopisenja.floodmonitoring.activity.OtherMarkerActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class HistoryButtonTest {

    @Rule
    public ActivityTestRule<OtherMarkerActivity> activityTestRule = new ActivityTestRule<>(OtherMarkerActivity.class);

    @Test
    public void testHistory() throws InterruptedException {
        Thread.sleep(2000);
        onView(withId(R.id.recycleView_other)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(2000);
        onView(withId(R.id.recycleView_history)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
