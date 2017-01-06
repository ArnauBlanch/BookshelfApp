package com.mingo_blanch.pr_idi.bookshelf_app.EditPersonalEvaluation;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.R;

import java.util.List;

/**
 * Created by ivan on 01/01/2017.
 */

public class PersEvalDialogFragment extends DialogFragment {

    private long bookId;
    private String persEval;
    private PersEvalDialogFragment.PersEvalListener listener;

    public interface PersEvalListener {
        void onEditPersEvalConfirmed(final Book book, String persEval);
    }

    public static PersEvalDialogFragment newInstance(long bookId) {
        PersEvalDialogFragment pedf = new PersEvalDialogFragment();

        Bundle args = new Bundle();
        args.putLong("bookId", bookId);
        pedf.setArguments(args);

        return pedf;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BookData bookData;
        final EditText editText;

        bookId = getArguments().getLong("bookId");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.pers_eval_dialog, null);

        // Set the edit text content with the actual bookId personal evaluation
        bookData = new BookData(getActivity());
        bookData.open();
        List<Book> books = bookData.getAllBooks();
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        Book book = new Book();
        boolean found = false;
        for (int i = 0; i < books.size() && !found; i++) {
            book = books.get(i);
            if (book.getId() == bookId) {
                found = true;
                persEval = book.getPersonal_evaluation();
                radioGroup.check(personalEvaluationToId(persEval));
                builder.setTitle(book.getTitle() + " (" + book.getYear() + ")");
            }
        }
        final Book a = book;

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the DB with the nuew personal evaluation for bookId
                        String newPersEval = idToPersonalEvaluation(radioGroup.getCheckedRadioButtonId());
                        bookData.updatePersonalEvaluation(bookId, newPersEval);
                        a.setPersonal_evaluation(newPersEval);
                        listener.onEditPersEvalConfirmed(a, persEval);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    private int personalEvaluationToId(String persEval){
        if (persEval.equals(getString(R.string.very_good)))
            return R.id.radioButton;
        else if (persEval.equals(getString(R.string.good)))
            return R.id.radioButton2;
        else if (persEval.equals(getString(R.string.regular)))
            return R.id.radioButton3;
        else if (persEval.equals(getString(R.string.bad)))
            return R.id.radioButton4;
        else
            return R.id.radioButton5;
    }

    private String idToPersonalEvaluation(int id) {
        switch (id) {
            case R.id.radioButton :
                return getString(R.string.very_good);
            case R.id.radioButton2 :
                return getString(R.string.good);
            case R.id.radioButton4 :
                return getString(R.string.bad);
            case R.id.radioButton5 :
                return getString(R.string.very_bad);
            default :
                return getString(R.string.regular);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (PersEvalDialogFragment.PersEvalListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
