package com.example.pr_idi.mydatabaseexample.DeleteBook;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.*;


public class DeleteDialogFragment extends DialogFragment {

    private long bookId;
    private BookData bookData;
    private Book book;

    public interface DeleteDialogListener {
        void onBookDeleted(final Book b);
    }

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

        bookData = new BookData(getActivity());
        bookData.open();

        bookId = getArguments().getLong("bookId");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DeleteDialogTheme);
        builder.setMessage(R.string.dialog_delete_confirmation)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity) getActivity()).onBookDeleteConfirmed(book);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Nothing
                    }
                });
        builder.setTitle(R.string.delete_title);

        book = bookData.getBookById(bookId);
        Log.v("Book:", book.toString());
        View bookDetailsView = getActivity().getLayoutInflater().inflate(R.layout.delete_book_details, null);

        ((TextView)bookDetailsView.findViewById(R.id.book_title)).setText(book.getTitle());
        ((TextView)bookDetailsView.findViewById(R.id.book_author)).setText(book.getAuthor());
        ((TextView)bookDetailsView.findViewById(R.id.book_year)).setText("("+String.valueOf(book.getYear())+")");
        ((TextView)bookDetailsView.findViewById(R.id.book_publisher)).setText(book.getPublisher());
        ((TextView)bookDetailsView.findViewById(R.id.book_persEval)).setText(book.getPersonal_evaluation());

        builder.setView(bookDetailsView);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
