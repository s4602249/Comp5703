package comp5703.sydney.edu.au.comp5703_tracker_app.Pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import comp5703.sydney.edu.au.comp5703_tracker_app.R
import org.junit.*
import org.junit.runner.RunWith


//UI Testing with Espresso
@RunWith(AndroidJUnit4ClassRunner::class)
class RegisterActivityTest {

    //this variable will global for all fun that we will create
    @get:Rule
    var mActivityTestRule: ActivityTestRule<RegisterActivity> = ActivityTestRule<RegisterActivity>(
        RegisterActivity::class.java
    )
    var registerActivity: RegisterActivity? = null




    //if display the correct page
    @Test
    fun checkActivityVisibility() {
        onView(withId(R.id.layout_register))
            .check(matches(isDisplayed()))
    }

    //if all the widgets are displayed
    @Test
    fun checkWidgetsVisibility() {
        onView(withId(R.id.userNameInput)).check(matches(isDisplayed()))
        onView(withId(R.id.EmailInput)).check(matches(isDisplayed()))
        onView(withId(R.id.PasswordInput)).check(matches(isDisplayed()))
        onView(withId(R.id.PhoneInput)).check(matches(isDisplayed()))
        onView(withId(R.id.DOBInput)).check(matches(isDisplayed()))
        onView(withId(R.id.Date_Picker)).check(matches(isDisplayed()))
        onView(withId(R.id.button2)).check(matches(isDisplayed()))
        onView(withId(R.id.BackImageButton)).check(matches(isDisplayed()))
    }

    //register
    @Test
    fun checkRegSuccessfully() {
        onView(withId(R.id.userNameInput)).perform(ViewActions.replaceText("EsTest"))
        onView(withId(R.id.userNameInput)).check(matches(withText("EsTest")));
        onView(withId(R.id.EmailInput)).perform(ViewActions.replaceText("EsTest@qq.com"))
        onView(withId(R.id.EmailInput)).check(matches(withText("EsTest@qq.com")))
        onView(withId(R.id.PasswordInput)).perform(ViewActions.replaceText("123456"))
        onView(withId(R.id.PasswordInput)).check(matches(withText("123456")))
        onView(withId(R.id.PhoneInput)).perform(ViewActions.replaceText("1234567"))
        onView(withId(R.id.PhoneInput)).check(matches(withText("1234567")))
        onView(withId(R.id.DOBInput)).perform(ViewActions.replaceText("08/05/2022"))
        onView(withId(R.id.DOBInput)).check(matches(withText("08/05/2022")))
        onView(withId(R.id.button2)).perform(ViewActions.click())
        onView(withId(R.id.layout_register)).check(matches(isDisplayed()))
    }
}