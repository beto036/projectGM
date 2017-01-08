package com.example.admin.myexample;

import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.example.admin.myexample.notes.view.NotesActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by admin on 1/8/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NotesTest {

    @Rule
    public ActivityTestRule<NotesActivity> notesActivityTestRule = new ActivityTestRule<NotesActivity>(NotesActivity.class);

    @Test
    public void clickOnAddNoteTest(){
        onView(withId(R.id.fabAddNewNote)).perform(click());
        onView(withId(R.id.addNoteTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnNoteTest(){
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.noteDetailsTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollAndClickOnNoteTest(){
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.actionOnItemAtPosition(3,click()));
        onView(withId(R.id.noteDetailsTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void addNoteTest(){
        String title = "New Note by Espresso";
        String description = "This is a test note created with instrumentation testing";
        onView(withId(R.id.fabAddNewNote)).perform(click());
        onView(withId(R.id.addNoteTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.addNoteTitle)).perform(typeText(title), closeSoftKeyboard());
        onView(withId(R.id.addNoteDesc)).perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.addNoteFab)).perform(click());
        onView(withId(R.id.notesRecycler)).check(matches(isDisplayed()));
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(title))));
        onView(withText(title)).check(matches(isDisplayed()));
    }

    @Test
    public void editNoteTest(){
        String title = "Note Updated";
        String description = "This is a test note updated with instrumentation testing";
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.noteDetailsTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.editNoteFab)).perform(click());
        onView(withId(R.id.editNoteTitle)).perform(clearText()).perform(typeText(title), closeSoftKeyboard());
        onView(withId(R.id.editNoteDesc)).perform(clearText()).perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.editNoteFabDone)).perform(click());
        onView(withId(R.id.notesRecycler)).check(matches(isDisplayed()));
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(title))));
        onView(withText(title)).check(matches(isDisplayed()));
    }

    @Test
    public void addNoteErrorsTest(){
        String error = "This field cannot be empty";
        onView(withId(R.id.fabAddNewNote)).perform(click());
        onView(withId(R.id.addNoteTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.addNoteFab)).perform(click());
        onView(withId(R.id.addNoteTitleLayout)).check(matches(hasErrorText(error)));
        onView(withId(R.id.addNoteDescLayout)).check(matches(hasErrorText(error)));
    }

    @Test
    public void editNoteErrorTest(){
        String error = "This field cannot be empty";
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.noteDetailsTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.editNoteFab)).perform(click());
        onView(withId(R.id.editNoteTitle)).perform(clearText());
        onView(withId(R.id.editNoteDesc)).perform(clearText());
        onView(withId(R.id.editNoteFabDone)).perform(click());
        onView(withId(R.id.editNoteTitleLayout)).check(matches(hasErrorText(error)));
        onView(withId(R.id.editNoteDescLayout)).check(matches(hasErrorText(error)));
    }

    public static Matcher<View> hasErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
