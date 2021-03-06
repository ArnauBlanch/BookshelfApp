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
import android.widget.RatingBar;
import android.widget.TextView;

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
        final RatingBar ratingBar;
        final TextView ratingBarText;

        bookId = getArguments().getLong("bookId");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.pers_eval_dialog, null);

        // Set the edit text content with the actual bookId personal evaluation
        bookData = new BookData(getActivity());
        bookData.open();
        List<Book> books = bookData.getAllBooks();
        // Set the rating bar and the text view below it
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        ratingBarText = (TextView) view.findViewById(R.id.rating_bar_text);
        setOnRatingBarChangeListener(ratingBar, ratingBarText);

        // Get the actual book evaluation and show it in the rating bar and text view
        Book book = new Book();
        boolean found = false;
        for (int i = 0; i < books.size() && !found; i++) {
            book = books.get(i);
            if (book.getId() == bookId) {
                found = true;
                // Get book personal evaluation
                persEval = book.getPersonal_evaluation();
                // Set rating
                ratingBar.setRating((float) personalEvaluationToRating(persEval));
                // Set text
                ratingBarText.setText(persEval.toUpperCase());
                // Set title
                builder.setTitle(book.getTitle() + " (" + book.getYear() + ")");
            }
        }
        final Book a = book;

        // Set the positive and negative button behaviour
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the DB with the nuew personal evaluation for bookId
                        String newPersEval = ratingToPersonalEvaluation((int) ratingBar.getRating());
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

    private int personalEvaluationToRating(String persEval){
        if (persEval.equals(getString(R.string.very_good)))
            return 5;
        else if (persEval.equals(getString(R.string.good)))
            return 4;
        else if (persEval.equals(getString(R.string.regular)))
            return 3;
        else if (persEval.equals(getString(R.string.bad)))
            return 2;
        else
            return 1;
    }

    private String ratingToPersonalEvaluation(int rate) {
        switch (rate) {
            case 5 :
                return getString(R.string.very_good);
            case 4 :
                return getString(R.string.good);
            case 3 :
                return getString(R.string.regular);
            case 2 :
                return getString(R.string.bad);
            default :
                return getString(R.string.very_bad);
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

    private void setOnRatingBarChangeListener(RatingBar ratingBar, final TextView textView) {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                textView.setText((ratingToPersonalEvaluation((int)v)).toUpperCase());
            }
        });
    }

}
