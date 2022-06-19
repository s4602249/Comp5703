package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//UI Testing with Espresso
@RunWith(AndroidJUnit4ClassRunner::class)
class ShowUserInfoTest{

    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<ShowUserInfo> = ActivityTestRule<ShowUserInfo>(
        ShowUserInfo::class.java
    )

    //if display the correct page
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_showUserInfo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //if all the widgets are displayed
    @Test
    fun checkWidgetVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.profile_image3))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}