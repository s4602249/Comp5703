package comp5703.sydney.edu.au.comp5703_tracker_app.Pages.setting

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

@RunWith(AndroidJUnit4ClassRunner::class)
class SettingAddGoalActivityTest {
    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<SettingAddGoalActivity> = ActivityTestRule<SettingAddGoalActivity>(
        SettingAddGoalActivity::class.java
    )


    //check whether the title, input boxes and buttons are displayed
    @Test
    fun checkComponentsVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.addGoalLabel))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.editTextGoalTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.editTextAddGoalScore))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.editTextAddGoalComment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddGoalConfirm))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddGoalBack))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    //check the demonstration that add goal with empty goal name
    @Test
    fun checkAddGoalActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.editTextGoalTitle))
            .perform(ViewActions.replaceText(""));
        Espresso.onView(ViewMatchers.withId(R.id.editTextGoalTitle))
            .check(ViewAssertions.matches(ViewMatchers.withText("")));

        Espresso.onView(ViewMatchers.withId(R.id.editTextAddGoalScore))
            .perform(ViewActions.replaceText("321"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextAddGoalScore))
            .check(ViewAssertions.matches(ViewMatchers.withText("321")));

        Espresso.onView(ViewMatchers.withId(R.id.editTextAddGoalComment))
            .perform(ViewActions.replaceText("UI test Goal"));
        Espresso.onView(ViewMatchers.withId(R.id.editTextAddGoalComment))
            .check(ViewAssertions.matches(ViewMatchers.withText("UI test Goal")));
        // Click confirm button
        Espresso.onView(ViewMatchers.withId(R.id.buttonAddGoalConfirm)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.editTextAddGoalScore))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}