package comp5703.sydney.edu.au.comp5703_tracker_app.Pages



import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import java.lang.Exception

import androidx.test.espresso.action.ViewActions.*


//UI Testing with Espresso
@RunWith(AndroidJUnit4ClassRunner::class)
class LoginActivityTest {

    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<LoginActivity> = ActivityTestRule<LoginActivity>(
        LoginActivity::class.java
    )

    var loginActivity: LoginActivity? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        loginActivity = mActivityTestRule.activity
    }

    //if display the correct page
    @Test
    fun checkActivityVisibility() {
        onView(withId(R.id.layout_loginActivity)).check(matches(isDisplayed()))
    }

    //if all the widgets are displayed
    @Test
    fun checkBtnVisibility() {
        onView(withId(R.id.Scanner)).check(matches(isDisplayed()))
        onView(withId(R.id.LogInEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.LogInPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.Google_login)).check(matches(isDisplayed()))
        onView(withId(R.id.button)).check(matches(isDisplayed()))
        onView(withId(R.id.register_btn)).check(matches(isDisplayed()))
    }

    //check textview display correct or not
    @Test
    fun checkTextViewActivity() {
        onView(withId(R.id.login_textview)).check(matches(withText("Creator Login")))
    }

    //check click register button
    @Test
    fun checkClickRegister() {
        onView(withId(R.id.register_btn)).perform(click())
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
    }


    //wrong email and pwd
    @Test
    fun checkClickLoginWithWrongCredential() {
        onView(withId(R.id.LogInEmail)).perform(replaceText("zxf@qq.co"))
        onView(withId(R.id.LogInEmail)).check(matches(withText("zxf@qq.co")));
        onView(withId(R.id.LogInPassword)).perform( replaceText("12345"))
        onView(withId(R.id.LogInPassword)).check(matches(withText("12345")))
        // Click login button
        onView(withId(R.id.button)).perform(click())
        onView(withId(R.id.layout_loginActivity)).check(matches(isDisplayed()))
    }


    //check login button
    @Test
    fun checkClickLoginWithCorrectCredential() {
        onView(withId(R.id.LogInEmail)).perform(replaceText("zxf@qq.com"));
        onView(withId(R.id.LogInEmail)).check(matches(withText("zxf@qq.com")));
        onView(withId(R.id.LogInPassword)).perform( replaceText("123456"));
        onView(withId(R.id.LogInPassword)).check(matches(withText("123456")));
        // Click login button
        onView(withId(R.id.button)).perform(click())
    }
}