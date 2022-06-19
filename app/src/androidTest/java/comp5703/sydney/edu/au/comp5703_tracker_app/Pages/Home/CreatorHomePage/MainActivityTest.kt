package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage


import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule

import comp5703.sydney.edu.au.comp5703_tracker_app.R


import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



//UI Testing with Espresso
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(
        MainActivity::class.java
    )

    //test if the page is loaded successfully
    @Test
    fun checkActivityVisibility() {
        onView(withId(R.id.layout_Mainpage))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    //test if add button can be click
    @Test
    fun checkClickAddMember() {
        onView(withId(R.id.AddMember)).perform(click())
        onView(withId(R.id.btm)).check(ViewAssertions.matches(isDisplayed()))
    }

    //test if the listview is showed
    @Test
    fun checkListview() {
        onView(withId(R.id.listOfRole)).check(ViewAssertions.matches(isDisplayed()))
    }

    //check if the navigation btn is showed
    @Test
    fun checkNavIsShowed() {
        onView(withId(R.id.buttom_navigation))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}


