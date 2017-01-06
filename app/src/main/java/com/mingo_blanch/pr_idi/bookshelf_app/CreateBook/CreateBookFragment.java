package com.mingo_blanch.pr_idi.bookshelf_app.CreateBook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.transition.Fade;
import android.util.Log;
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
import android.widget.TextView;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.MainActivity;
import com.mingo_blanch.pr_idi.bookshelf_app.R;

public class CreateBookFragment extends Fragment {

    private TextInputLayout titleLayout;
    private EditText titleEditText;
    private TextInputLayout authorLayout;
    private AutoCompleteTextView authorEditText;
    private TextInputLayout yearLayout;
    private EditText yearEditText;
    private TextInputLayout publisherLayout;
    private AutoCompleteTextView publisherEditText;
    private TextInputLayout categoryLayout;
    private AutoCompleteTextView categoryEditText;
    private AppCompatSpinner persEvalSpinner;
    private LinearLayout parentLayout;

    private Animation animShake;


    private BookData bookData;

    private CreateBookFragmentListener listener;
    public interface CreateBookFragmentListener {
        void onBookCreated(final Book b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);    // Save fragment across config changes
        setHasOptionsMenu(true);    // Fragment has menu items to display

        setupWindowAnimations();

        bookData = BookData.getInstance(getActivity());
        bookData.open();

        View view = inflater.inflate(R.layout.fragment_create_book, container, false);

        titleLayout = (TextInputLayout) view.findViewById(R.id.titleLayout);
        titleEditText = (EditText) view.findViewById(R.id.title);
        authorLayout = (TextInputLayout) view.findViewById(R.id.authorLayout);
        authorEditText = (AutoCompleteTextView) view.findViewById(R.id.author);
        yearLayout = (TextInputLayout) view.findViewById(R.id.yearLayout);
        yearEditText = (EditText) view.findViewById(R.id.year);
        publisherLayout = (TextInputLayout) view.findViewById(R.id.publisherLayout);
        publisherEditText = (AutoCompleteTextView) view.findViewById(R.id.publisher);
        categoryLayout = (TextInputLayout) view.findViewById(R.id.categoryLayout);
        categoryEditText = (AutoCompleteTextView) view.findViewById(R.id.category);
        persEvalSpinner = (AppCompatSpinner) view.findViewById(R.id.evaluation);

        prepareAutoCompleteSuggestions();

        parentLayout = (LinearLayout) view.findViewById(R.id.parentlayout);
        setupUI(parentLayout);

        final TextView evalLabel = (TextView) view.findViewById(R.id.evalLabel);
        persEvalSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                evalLabel.setVisibility(View.VISIBLE);
                Log.v("test", "TOUCHED!");
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
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
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

        Log.v("creation", "Correct fields");

        titleLayout.setErrorEnabled(false);
        authorLayout.setErrorEnabled(false);
        yearLayout.setErrorEnabled(false);
        categoryLayout.setErrorEnabled(false);
        publisherLayout.setErrorEnabled(false);
//        persEvalLayout.setErrorEnabled(false);

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
        /*if (!checkPersEval()) {
            //animateEditText(persEvalLayout);
            b = false;
        }*/

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CreateBookFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
