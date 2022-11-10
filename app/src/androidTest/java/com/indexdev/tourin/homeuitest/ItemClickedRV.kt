package com.indexdev.tourin.homeuitest


import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
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
class ItemClickedRV {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_BACKGROUND_LOCATION"
        )

    @Test
    fun itemClickedRV() {
        Thread.sleep(6000)
//        val appCompatImageView = onView(
//            allOf(
//                withId(R.id.btn_next),
//                isDisplayed()
//            )
//        )
//        appCompatImageView.perform(click())
//
//        val appCompatImageView2 = onView(
//            allOf(
//                withId(R.id.btn_next),
//                isDisplayed()
//            )
//        )
//        appCompatImageView2.perform(click())
//
//        val materialButton = onView(
//            allOf(
//                withId(R.id.btn_next), withText("Get Started"),
//                isDisplayed()
//            )
//        )
//        materialButton.perform(click())
//
//        val textInputEditText = onView(
//            allOf(
//                withId(R.id.et_email),
//                isDisplayed()
//            )
//        )
//        textInputEditText.perform(replaceText("userandroidtest1@mail.com"), closeSoftKeyboard())
//
//        val textInputEditText2 = onView(
//            allOf(
//                withId(R.id.et_email), withText("userandroidtest1@mail.com"),
//                isDisplayed()
//            )
//        )
//        textInputEditText2.perform(pressImeActionButton())
//
//        val textInputEditText3 = onView(
//            allOf(
//                withId(R.id.et_password),
//                isDisplayed()
//            )
//        )
//        textInputEditText3.perform(replaceText("1234567"), closeSoftKeyboard())
//
//        val textInputEditText4 = onView(
//            allOf(
//                withId(R.id.et_password), withText("1234567"),
//                isDisplayed()
//            )
//        )
//        textInputEditText4.perform(pressImeActionButton())
//
//        val materialButton2 = onView(
//            allOf(
//                withId(R.id.btn_login), withText("Log In"),
//                isDisplayed()
//            )
//        )
//        materialButton2.perform(click())

        Thread.sleep(6000)
        val recyclerView = onView(
            allOf(
                withId(R.id.rv_popular_tour),
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

//        val materialButton3 = onView(
//            allOf(
//                withId(android.R.id.button1), withText("Grant background Permission"),
//            )
//        )
//        materialButton3.perform(scrollTo(), click())
//        val device: UiDevice = UiDevice.getInstance(getInstrumentation())
//        val marker: UiObject = device.findObject(UiSelector().descriptionContains("marker title"))
//        marker.click()

        Thread.sleep(6000)
        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_route), withText("Route"),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
    }
}
