package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.Home.ParentHomePage

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
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
class ParentAddKidAndParentTest{
    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<ParentAddKidAndParent> = ActivityTestRule<ParentAddKidAndParent>(
        ParentAddKidAndParent::class.java
    )

    //if display the correct page
    @Test
    fun checkActivityVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.btm))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //if all the widgets are displayed
    @Test
    fun checkBtnVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.familyNameInput))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.radioGroup))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.Phone_input))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.DOB_input))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    //add role with empty name
    @Test
    fun checkAddActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.familyNameInput))
            .perform(ViewActions.replaceText(""));
        Espresso.onView(ViewMatchers.withId(R.id.familyNameInput))
            .check(ViewAssertions.matches(ViewMatchers.withText("")));

        Espresso.onView(ViewMatchers.withId(R.id.Phone_input))
            .perform(ViewActions.replaceText("123456"));
        Espresso.onView(ViewMatchers.withId(R.id.Phone_input))
            .check(ViewAssertions.matches(ViewMatchers.withText("123456")));

        Espresso.onView(ViewMatchers.withId(R.id.DOB_input))
            .perform(ViewActions.replaceText("08/05/2022"));
        Espresso.onView(ViewMatchers.withId(R.id.DOB_input))
            .check(ViewAssertions.matches(ViewMatchers.withText("08/05/2022")));
        // Click login button
        Espresso.onView(ViewMatchers.withId(R.id.btn_save)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btm))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}