package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity
        implements ListCategoryFragment.ListCategoryFragmentListener, CreateBookFragment.CreateBookFragmentListener {

    private BookData bookData;

    private ListCategoryFragment listCategoryFragment;
    private CreateBookFragment createBookFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (bookData == null) bookData = new BookData(this);

        bookData.open();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            this.overridePendingTransition(R.anim.left_right,
                    R.anim.right_left);

            // Create a new Fragment to be placed in the activity layout
            listCategoryFragment = new ListCategoryFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            listCategoryFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, listCategoryFragment);
            transaction.commit();

        }
    }

    public void onBookSelected(long rowID) {
        Log.i("MainActivity","Book selected");
    }

    public void onAddBook() {
        Log.i("MainActivity","Add book");


        createBookFragment = new CreateBookFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
        //        R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.fragment_container, createBookFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onBookCreate(String title, String author, Integer year, String publisher, String category, String persEval) {
        Log.i("MainActivity", "Creating book");

        bookData.createBook(title, author, year, publisher, category, persEval);
        listCategoryFragment.updateBooks();

        getSupportFragmentManager().popBackStack();
    }


    @Override
    public void onBackPressed() {


        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.i("BACK", "Button pressed - "+count);

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        bookData.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        bookData.close();
    }

    public BookData getBookData() {
        return bookData;
    }

    public void deleteBookConfirmed(long id) {
        bookData.deleteBookById(id);
        listCategoryFragment.updateBooks();
    }
}