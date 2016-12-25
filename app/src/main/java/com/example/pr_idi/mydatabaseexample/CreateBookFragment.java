package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CreateBookFragment extends Fragment {

    private final String[] evaluations = {"very good", "good", "regular", "bad", "very bad"};

    private TextInputLayout titleLayout;
    private EditText titleEditText;
    private TextInputLayout authorLayout;
    private EditText authorEditText;
    private TextInputLayout yearLayout;
    private EditText yearEditText;
    private TextInputLayout publisherLayout;
    private EditText publisherEditText;
    private TextInputLayout persEvalLayout;
    private EditText persEvalEditText;
    private TextInputLayout categoryLayout;
    private EditText categoryEditText;
    private LinearLayout parentLayout;

    private Animation animShake;


    public interface CreateBookFragmentListener {
        void onBookCreate(String title, String author, Integer year, String publisher, String category, String persEval);
    }

    private CreateBookFragmentListener listener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CreateBookFragmentListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);    // Save fragment across config changes
        setHasOptionsMenu(true);    // Fragment has menu items to display

        setupWindowAnimations();

        View view = inflater.inflate(R.layout.fragment_create_book, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack();
            }
        });
        setHasOptionsMenu(true);

        titleLayout = (TextInputLayout) view.findViewById(R.id.titleLayout);
        titleEditText = (EditText) view.findViewById(R.id.title);
        authorLayout = (TextInputLayout) view.findViewById(R.id.authorLayout);
        authorEditText = (EditText) view.findViewById(R.id.author);
        yearLayout = (TextInputLayout) view.findViewById(R.id.yearLayout);
        yearEditText = (EditText) view.findViewById(R.id.year);
        publisherLayout = (TextInputLayout) view.findViewById(R.id.publisherLayout);
        publisherEditText = (EditText) view.findViewById(R.id.publisher);
        categoryLayout = (TextInputLayout) view.findViewById(R.id.categoryLayout);
        categoryEditText = (EditText) view.findViewById(R.id.category);
        persEvalLayout = (TextInputLayout) view.findViewById(R.id.evaluationLayout);
        persEvalEditText = (EditText) view.findViewById(R.id.evaluation);

        parentLayout = (LinearLayout) view.findViewById(R.id.parentlayout);
        animShake = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shake);

        setupUI(parentLayout);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bookSaveButton:
                saveListener();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getActivity().getWindow().setExitTransition(fade);
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getView().getWindowToken(), 0);

    }

    public void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    parentLayout.requestFocus();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void saveListener() {
        if (!checkCorrectFields())
            return;

        titleLayout.setErrorEnabled(false);
        authorLayout.setErrorEnabled(false);
        yearLayout.setErrorEnabled(false);
        categoryLayout.setErrorEnabled(false);
        publisherLayout.setErrorEnabled(false);
        persEvalLayout.setErrorEnabled(false);

        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        Integer year = Integer.parseInt(yearEditText.getText().toString().trim());
        String publisher = publisherEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();
        String persEval = persEvalEditText.getText().toString().trim();

        listener.onBookCreate(title, author, year, publisher, category, persEval);
    }

    // Comprovació de camps
    private boolean checkCorrectFields() {
        boolean b = true;
        if (!checkTitle()) {
            animateEditText(titleLayout);
            b = false;
        }
        if (!checkAuthor()) {
            animateEditText(authorLayout);
            b = false;
        }
        if (!checkYear()) {
            animateEditText(yearLayout);
            b = false;
        }
        if (!checkPublisher()) {
            animateEditText(publisherLayout);
            b = false;
        }
        if (!checkCategory()) {
            animateEditText(categoryLayout);
            b = false;
        }
        if (!checkPersEval()) {
            animateEditText(persEvalLayout);
            b = false;
        }

        return b;
    }


    // Comprovació camp a camp

    private boolean checkTitle() {
        if (titleEditText.getText().toString().trim().isEmpty()) {
            setErrors(titleLayout, titleEditText, R.string.msg_title);
            return false;
        }
        titleLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkAuthor() {
        if (authorEditText.getText().toString().trim().isEmpty()) {
            setErrors(authorLayout, authorEditText, R.string.msg_author);
            return false;
        }
        authorLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkYear() {
        String year = yearEditText.getText().toString().trim();
        boolean isNumber = year.matches("[-+]?\\d*\\.?\\d+");
        if (year.isEmpty() || !isNumber) {
            setErrors(yearLayout, yearEditText, R.string.msg_year);
            return false;
        }

        yearLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPublisher() {
        if (publisherEditText.getText().toString().trim().isEmpty()) {
            setErrors(publisherLayout, publisherEditText, R.string.msg_publisher);
            return false;
        }
        publisherLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkCategory() {
        if (categoryEditText.getText().toString().trim().isEmpty()) {
            setErrors(categoryLayout, categoryEditText, R.string.msg_category);
            return false;
        }
        categoryLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPersEval() {
        String persEval = persEvalEditText.getText().toString().trim();
        if (persEval.isEmpty()) {
            setErrors(persEvalLayout, persEvalEditText, R.string.msg_pers_eval);
            return false;
        }

        for (String eval : evaluations) {
            if (persEval.equals(eval)) {
                persEvalLayout.setErrorEnabled(false);
                return true;
            }
        }
        setErrors(persEvalLayout, persEvalEditText, R.string.msg_pers_eval);
        return false;

    }


    // Extras

    private void setErrors(TextInputLayout layout, EditText editText, int msg) {
        layout.setErrorEnabled(true);
        layout.setError(getString(msg));
        editText.setError(getString(R.string.error_msg_required));
    }

    private void animateEditText(TextInputLayout layout) {
        layout.setAnimation(animShake);
        layout.startAnimation(animShake);
    }
}
