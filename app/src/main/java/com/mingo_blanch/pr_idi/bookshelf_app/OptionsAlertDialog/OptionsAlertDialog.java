package com.mingo_blanch.pr_idi.bookshelf_app.OptionsAlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.DeleteBook.DeleteDialogFragment;
import com.mingo_blanch.pr_idi.bookshelf_app.EditPersonalEvaluation.PersEvalDialogFragment;

/**
 * Created by ivan on 04/01/2017.
 */

public class OptionsAlertDialog extends AlertDialog {

    private Context context;
    private long id;

    public OptionsAlertDialog(Context context, long id) {
        super(context);
        this.context = context;
        this.id = id;
    }

    public void showBuilder() {
        CharSequence[] options = new CharSequence[] {"Edita valoració personal", "Elimina llibre"};
        BookData bookData = new BookData(context);
        bookData.open();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        // Set dialog title
        for (Book a : bookData.getAllBooks()) {
            if (a.getId() == this.id) {
                builder.setTitle(a.getTitle() + " (" + a.getYear() + ")");
            }
        }
        // Set dialog items
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                // Prepare dialogs transaction
                ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                Fragment prev = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.addToBackStack(null);

                DialogFragment newFragment = new DialogFragment();
                switch (position) {
                    case 0 :
                        newFragment = PersEvalDialogFragment.newInstance(id);
                        break;
                    case 1 :
                        newFragment = DeleteDialogFragment.newInstance(id);
                        break;
                }
                // Show the selected dialog
                newFragment.show(transaction, "dialog");
            }
        });
        builder.show();
    }
}
