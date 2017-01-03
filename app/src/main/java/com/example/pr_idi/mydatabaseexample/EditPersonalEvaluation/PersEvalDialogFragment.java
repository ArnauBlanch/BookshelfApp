package com.example.pr_idi.mydatabaseexample.EditPersonalEvaluation;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.Book;
import com.example.pr_idi.mydatabaseexample.BookData;
import com.example.pr_idi.mydatabaseexample.R;

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
        //final View view = inflater.inflate(R.layout.pers_eval_dialog, null);

        // Set the edit text content with the actual bookId personal evaluation
        bookData = new BookData(getActivity());
        bookData.open();
        List<Book> books = bookData.getAllBooks();
        /*editText = (EditText) view.findViewById(R.id.pers_eval);
        for (Book a : books) {
            if (a.getId() == bookId) {
                editText.setText(a.getPersonal_evaluation());
                builder.setTitle(a.getTitle() + " (" + a.getYear() + ")");
            }
        }

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the DB with the nuew personal evaluation for bookId
                        bookData.updatePersonalEvaluation(bookId, editText.getText().toString());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "S'ha modificat la valoració personal",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No s'ha modificat la valoració personal",
                                Toast.LENGTH_SHORT).show();
                    }
                });*/
        return builder.create();
    }


}
