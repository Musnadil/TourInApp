package com.indexdev.tourin.authuitest


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.indexdev.tourin.MainActivity
import com.indexdev.tourin.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterUITest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun registerUITest() {
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

        val materialTextView = onView(
            allOf(
                withId(R.id.btn_create_account), withText("Create"),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.et_username),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("userandroidtest1"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_email),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("userandroidtest1@mail.com"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.et_email), withText("userandroidtest1@mail.com"),
                isDisplayed()
            )
        )
        textInputEditText3.perform(pressImeActionButton())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.et_password),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("1234567"), closeSoftKeyboard())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.et_password), withText("1234567"),
                isDisplayed()
            )
        )
        textInputEditText5.perform(pressImeActionButton())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.et_confirm_password),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("1234567"), closeSoftKeyboard())

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.et_confirm_password), withText("1234567"),
                isDisplayed()
            )
        )
        textInputEditText7.perform(pressImeActionButton())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_regist), withText("Register"),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
            )
        )
        materialButton3.perform(scrollTo(), click())
    }
}
