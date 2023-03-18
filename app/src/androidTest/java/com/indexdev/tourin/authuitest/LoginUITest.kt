package com.indexdev.tourin.authuitest


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.indexdev.tourin.MainActivity
import com.indexdev.tourin.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginUITest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION"
        )

    @Test
    fun loginUITest() {
        Thread.sleep(6000)
        val appCompatImageView = onView(
            allOf(
                withId(R.id.btn_next),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.btn_next),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.btn_next), withText("Get Started"),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.et_email),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("userandroidtest1@mail.com"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_password),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("1234567"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.et_password), withText("1234567"),
                isDisplayed()
            )
        )
        textInputEditText3.perform(pressImeActionButton())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_login), withText("Log In"),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
    }

}
