package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.KidHomePage

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InvitedChildActivityTest{

    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<InvitedChildActivity> = ActivityTestRule<InvitedChildActivity>(
        InvitedChildActivity::class.java
    )

    //test if the page is loaded successfully
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.childName))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    //test if the listview is showed
    @Test
    fun checkListview() {
        Espresso.onView(ViewMatchers.withId(R.id.invited_Children_list))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //check if the navigation btn is showed
    @Test
    fun checkNavIsShowed() {
        Espresso.onView(ViewMatchers.withId(R.id.buttom_navigation))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}