package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class DeleteDialogFragment extends DialogFragment {

    private long bookId;


    public static DeleteDialogFragment newInstance(long bookId) {
        DeleteDialogFragment f = new DeleteDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("bookId", bookId);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        bookId = getArguments().getLong("bookId");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete_confirmation)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity)getActivity()).deleteBookConfirmed(bookId);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Nothing
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
