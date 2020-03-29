package com.kopisenja.floodmonitoring.UnitTesting;


import android.util.Log;
import android.view.View;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.activity.IndexActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class IndexTesting {

    @Rule
    public ActivityTestRule<IndexActivity> activityTestRule = new ActivityTestRule<>(IndexActivity.class);

    @Test
    public void testUiIndex() throws InterruptedException, UiObjectNotFoundException {
        Thread.sleep(2000);
//        onView(withContentDescription("Bojongsoang")).perform(click());
//        onView(isRoot()).perform(waitId())
//        onView(withContentDescription("Bojongsoang")).perform(click());
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("Bojongsoang"));
////        UiObject iot2 = uiDevice.findObject(new UiSelector().descriptionContains("Radio"));
        marker.click();
//        try {
//
            Thread.sleep(2000);
////            onView(isRoot()).perform(waitId(R.id.bottom_sheet_detail, TimeUnit.SECONDS.toMillis(15)));
////            onView(isRoot()).perform(waitId(R.id.bottom_sheet_category, TimeUnit.SECONDS.toMillis(15)));
////            onView(isRoot()).perform(waitId(R.id.bottom_sheet_dateTime, TimeUnit.SECONDS.toMillis(15)));
//        } catch (UiObjectNotFoundException e) {
//            Log.e("ANDROID_TEST_LOG", e.getMessage().toString());
//        }
    }

    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a spesific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }
                } while (System.currentTimeMillis() < endTime);

                //timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
