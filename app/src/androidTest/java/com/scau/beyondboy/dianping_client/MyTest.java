package com.scau.beyondboy.dianping_client;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Author:beyondboy
 * Gmail:xuguoli.scau@gmail.com
 * Date: 2015-08-30
 * Time: 00:11
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyTest
{
    @Rule
    public ActivityTestRule<WelcomeStartAct> testActivity=new ActivityTestRule<>(WelcomeStartAct.class);
    @Test
    public void test1()
    {
        onView(withId(R.id.test)).perform(click()).check(matches(withText("点击")));
      //  onView(withId(R.id.test)).perform(typeText("点击"));
    }
}