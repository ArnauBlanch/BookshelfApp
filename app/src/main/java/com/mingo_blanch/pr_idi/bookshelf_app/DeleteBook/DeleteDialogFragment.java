package com.mingo_blanch.pr_idi.bookshelf_app.DeleteBook;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.R;


public class DeleteDialogFragment extends DialogFragment {

    private Book book;
    private DeleteBookListener listener;

    public interface DeleteBookListener {
        void onBookDeleteConfirmed(final Book book);
    }

    public static DeleteDialogFragment newInstance(long bookId) {
        DeleteDialogFragment f = new DeleteDialogFragment();

        Bundle args = new Bundle();
        args.putLong("bookId", bookId);
        f.setArguments(args);

        return f;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BookData bookData = new BookData(getActivity());
        bookData.open();

        long bookId = getArguments().getLong("bookId");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        builder.setMessage(R.string.dialog_delete_confirmation)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onBookDeleteConfirmed(book);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No facis res
                    }
                });
        builder.setTitle(R.string.delete_title);

        book = bookData.getBookById(bookId);
        View bookDetailsView = getActivity().getLayoutInflater().inflate(R.layout.delete_book_details, null);

        ((TextView)bookDetailsView.findViewById(R.id.book_title)).setText(book.getTitle());
        ((TextView)bookDetailsView.findViewById(R.id.book_subtitle)).setText(book.getAuthor());
        ((TextView)bookDetailsView.findViewById(R.id.book_year)).setText("("+String.valueOf(book.getYear())+")");
        ((TextView)bookDetailsView.findViewById(R.id.book_publisher)).setText(book.getPublisher());
        ((TextView)bookDetailsView.findViewById(R.id.book_persEval)).setText(book.getPersonal_evaluation());

        builder.setView(bookDetailsView);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (DeleteBookListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
