package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.CreatorHomePage

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
class ShowClickUserInfoTest{

    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<ShowClickUserInfo> = ActivityTestRule<ShowClickUserInfo>(
        ShowClickUserInfo::class.java
    )

    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.layout_showUserInfo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkQrCode() {
        Espresso.onView(ViewMatchers.withId(R.id.QRcode))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


}