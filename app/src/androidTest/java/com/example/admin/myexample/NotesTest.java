package com.example.admin.myexample;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.admin.myexample.notes.view.NotesActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by admin on 1/8/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NotesTest {

    private int numNotes;

    @Rule
    public IntentsTestRule<NotesActivity> intentsRule = new IntentsTestRule<>(NotesActivity.class);

    @Before
    public void setUp(){
        Instrumentation.ActivityResult result = createImageCaptureActivityResultStub();
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
        numNotes = ((RecyclerView)intentsRule.getActivity().findViewById(R.id.notesRecycler)).getAdapter().getItemCount();
    }

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
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.scrollToPosition(numNotes-1));
        onView(withId(R.id.notesRecycler)).perform(RecyclerViewActions.actionOnItemAtPosition(numNotes-1,click()));
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

        onView(withId(R.id.addNoteImageBtn)).perform(click());

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
        onView(withId(R.id.editNoteImageBtn)).perform(click());
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

    private Instrumentation.ActivityResult createImageCaptureActivityResultStub() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(
                intentsRule.getActivity().getResources(), R.mipmap.ic_launcher));

        Intent resultData = new Intent();
        resultData.putExtras(bundle);

        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }
}
