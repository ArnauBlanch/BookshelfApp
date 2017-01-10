package com.mingo_blanch.pr_idi.bookshelf_app.AddBook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.MainActivity;
import com.mingo_blanch.pr_idi.bookshelf_app.R;

public class AddBookFragment extends Fragment {

    private LinearLayout titleLayout;
    private TextInputLayout titleTILayout;
    private EditText titleEditText;

    private LinearLayout authorLayout;
    private TextInputLayout authorTILayout;
    private AutoCompleteTextView authorEditText;

    private LinearLayout yearLayout;
    private TextInputLayout yearTILayout;
    private EditText yearEditText;

    private LinearLayout publisherLayout;
    private TextInputLayout publisherTILayout;
    private AutoCompleteTextView publisherEditText;

    private LinearLayout categoryLayout;
    private TextInputLayout categoryTILayout;
    private AutoCompleteTextView categoryEditText;

    private LinearLayout persEvalLayout;
    private TextInputLayout persEvalTILayout;
    private AppCompatSpinner persEvalSpinner;

    private LinearLayout parentLayout;
    private Animation animShake;


    private BookData bookData;

    private AddBookFragmentListener listener;
    public interface AddBookFragmentListener {
        void onBookCreated(final Book b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        setupWindowAnimations();

        bookData = BookData.getInstance(getActivity());
        bookData.open();

        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        // Handle screen rotation
        ScrollView sv = (ScrollView) view.findViewById(R.id.add_book_fragment);
        if (getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().heightPixels) {
            sv.setPaddingRelative(0, 96, 0, 0); // 48 dp = 96 px
        } else {
            sv.setPaddingRelative(0, 112, 0, 0); // 56 dp = 112 px
        }

        titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
        titleTILayout = (TextInputLayout) view.findViewById(R.id.titleTILayout);
        titleEditText = (EditText) view.findViewById(R.id.title);

        authorLayout = (LinearLayout) view.findViewById(R.id.authorLayout);
        authorTILayout = (TextInputLayout) view.findViewById(R.id.authorTILayout);
        authorEditText = (AutoCompleteTextView) view.findViewById(R.id.author);

        yearLayout = (LinearLayout) view.findViewById(R.id.yearLayout);
        yearTILayout = (TextInputLayout) view.findViewById(R.id.yearTILayout);
        yearEditText = (EditText) view.findViewById(R.id.year);

        publisherLayout = (LinearLayout) view.findViewById(R.id.publisherLayout);
        publisherTILayout = (TextInputLayout) view.findViewById(R.id.publisherTILayout);
        publisherEditText = (AutoCompleteTextView) view.findViewById(R.id.publisher);

        categoryLayout = (LinearLayout) view.findViewById(R.id.categoryLayout);
        categoryTILayout = (TextInputLayout) view.findViewById(R.id.categoryTILayout);
        categoryEditText = (AutoCompleteTextView) view.findViewById(R.id.category);

        persEvalLayout = (LinearLayout) view.findViewById(R.id.evalLayout);
        persEvalTILayout = (TextInputLayout) view.findViewById(R.id.evalTILayout);
        persEvalSpinner = (AppCompatSpinner) view.findViewById(R.id.evaluation);

        prepareAutoCompleteSuggestions();

        parentLayout = (LinearLayout) view.findViewById(R.id.parentlayout);
        setupUI(parentLayout);

        final TextView evalLabel = (TextView) view.findViewById(R.id.evalLabel);
        persEvalSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                evalLabel.setText(getString(R.string.pers_eval));
                return false;
            }
        });


        animShake = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.shake);


        String[] evaluations = getResources().getStringArray(R.array.evaluations);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, evaluations) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount()));
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        persEvalSpinner.setAdapter(adapter);
        persEvalSpinner.setSelection(adapter.getCount());



        getActivity().findViewById(R.id.fab_btn_creation_done).setVisibility(View.VISIBLE);

        // Set action bar title
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.create_book_app_bar_title));

        // TODO: Refactor + acabar part de gestió d'errors + 'unknown'/'desconengut'
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private void prepareAutoCompleteSuggestions() {
        // Authors
        String[] authors = bookData.getAuthorList();
        ArrayAdapter<String> authorAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, authors);
        authorEditText.setAdapter(authorAdapter);

        // Publishers
        String[] publishers = bookData.getPublisherList();
        ArrayAdapter<String> publisherAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, publishers);
        publisherEditText.setAdapter(publisherAdapter);

        // Categories
        String[] categories = bookData.getCategoryList();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, categories);
        categoryEditText.setAdapter(categoryAdapter);
    }

    @Override
    public void onDestroyView() {
        getActivity().findViewById(R.id.fab_btn_creation_done).setVisibility(View.GONE);
        super.onDestroyView();
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

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    parentLayout.requestFocus();
                    return false;
                }
            });
        }

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

        titleTILayout.setErrorEnabled(false);
        authorTILayout.setErrorEnabled(false);
        yearTILayout.setErrorEnabled(false);
        categoryTILayout.setErrorEnabled(false);
        publisherTILayout.setErrorEnabled(false);
        persEvalTILayout.setErrorEnabled(false);

        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        Integer year = Integer.parseInt(yearEditText.getText().toString().trim());
        String publisher = publisherEditText.getText().toString().trim();
        String category = categoryEditText.getText().toString().trim();
        String persEval = persEvalSpinner.getSelectedItem().toString().trim();

        Book b = bookData.createBook(title, author, year, publisher, category, persEval);
        listener.onBookCreated(b);
    }

    // Comprovació de camps
    private boolean checkCorrectFields() {
        boolean b = true;

        if (!checkTitle())
            b = false;
        if (!checkAuthor())
            b = false;
        if (!checkYear())
            b = false;
        if (!checkPublisher())
            b = false;
        if (!checkCategory())
            b = false;
        if (!checkPersEval())
            b = false;

        return b;
    }



    // Comprovació camp a camp

    private boolean checkTitle() {
        if (titleEditText.getText().toString().trim().isEmpty()) {
            setErrors(titleTILayout, titleEditText, R.string.msg_title);
            animateLayout(titleLayout);
            return false;
        }
        titleTILayout.setErrorEnabled(false);
        titleLayout.setAnimation(null);
        return true;
    }

    private boolean checkAuthor() {
        if (authorEditText.getText().toString().trim().isEmpty()) {
            setErrors(authorTILayout, authorEditText, R.string.msg_author);
            animateLayout(authorLayout);
            return false;
        }
        authorTILayout.setErrorEnabled(false);
        authorLayout.setAnimation(null);
        return true;
    }

    private boolean checkYear() {
        String year = yearEditText.getText().toString().trim();
        boolean isNumber = year.matches("[-+]?\\d*\\.?\\d+");
        if (year.isEmpty() || !isNumber) {
            setErrors(yearTILayout, yearEditText, R.string.msg_year);
            animateLayout(yearLayout);
            return false;
        }
        yearTILayout.setErrorEnabled(false);
        yearLayout.setAnimation(null);
        return true;
    }

    private boolean checkPublisher() {
        if (publisherEditText.getText().toString().trim().isEmpty()) {
            setErrors(publisherTILayout, publisherEditText, R.string.msg_publisher);
            animateLayout(publisherLayout);
            return false;
        }
        publisherTILayout.setErrorEnabled(false);
        publisherLayout.setAnimation(null);
        return true;
    }

    private boolean checkCategory() {
        if (categoryEditText.getText().toString().trim().isEmpty()) {
            setErrors(categoryTILayout, categoryEditText, R.string.msg_category);
            animateLayout(categoryLayout);
            return false;
        }
        categoryTILayout.setErrorEnabled(false);
        categoryLayout.setAnimation(null);
        return true;
    }

    private boolean checkPersEval() {
        if (persEvalSpinner.getSelectedItem().equals(getString(R.string.pers_eval))) {
            persEvalTILayout.setErrorEnabled(true);
            persEvalTILayout.setError(getString(R.string.msg_pers_eval));
            ((TextView) persEvalSpinner.getSelectedView()).setError("Required field");
            animateLayout(persEvalLayout);
            return false;
        }
        ((TextView) persEvalSpinner.getSelectedView()).setError(null);
        persEvalTILayout.setErrorEnabled(false);
        persEvalLayout.setAnimation(null);
        return true;
    }

    // Extras

    private void setErrors(TextInputLayout layout, EditText editText, int msg) {
        layout.setErrorEnabled(true);
        layout.setError(getString(msg));
        editText.setError(getString(R.string.error_msg_required));
    }

    private void animateLayout(LinearLayout layout) {
        layout.setAnimation(animShake);
        layout.startAnimation(animShake);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (AddBookFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
