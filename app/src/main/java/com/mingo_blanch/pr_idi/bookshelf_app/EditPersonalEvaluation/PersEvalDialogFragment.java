package com.mingo_blanch.pr_idi.bookshelf_app.EditPersonalEvaluation;


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
        for (Book a : books) {
            if (a.getId() == bookId) {
                radioGroup.check(personalEvaluationToId(a.getPersonal_evaluation()));
                builder.setTitle(a.getTitle() + " (" + a.getYear() + ")");
            }
        }

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the DB with the nuew personal evaluation for bookId
                        bookData.updatePersonalEvaluation(bookId,
                                idToPersonalEvaluation(radioGroup.getCheckedRadioButtonId()));
                        // TODO: show snackbar
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    private int personalEvaluationToId(String persEval){
        switch (persEval) {
            case "Very good" :
                return R.id.radioButton;
            case "Good" :
                return R.id.radioButton2;
            case "Bad" :
                return R.id.radioButton4;
            case "Very Bad" :
                return R.id.radioButton5;
            default :
                return R.id.radioButton3;
        }

    }

    private String idToPersonalEvaluation(int id) {
        switch (id) {
            case R.id.radioButton :
                return "Very good";
            case R.id.radioButton2 :
                return "Good";
            case R.id.radioButton4 :
                return "Bad";
            case R.id.radioButton5 :
                return "Very Bad" ;
            default :
                return "Regular";
        }
    }

}
