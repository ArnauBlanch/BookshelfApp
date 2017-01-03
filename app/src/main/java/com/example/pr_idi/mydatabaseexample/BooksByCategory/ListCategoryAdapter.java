package com.example.pr_idi.mydatabaseexample.BooksByCategory;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.Book;
import com.example.pr_idi.mydatabaseexample.BookData;
import com.example.pr_idi.mydatabaseexample.DeleteBook.DeleteDialogFragment;
import com.example.pr_idi.mydatabaseexample.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;


public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.BookViewHolder>{

    private List<Book> bookList;
    private BookData bookData;
    private ListCategoryFragment fragment;

    public ListCategoryAdapter(BookData bookData, ListCategoryFragment fragment) {
        this.bookData = bookData;
        bookList = bookData.getAllBooks();
        this.fragment = fragment;
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, int i) {
        Book book = bookList.get(i);
        bookViewHolder.setTitle(book.getTitle());
        bookViewHolder.setAuthor(book.getAuthor());
        bookViewHolder.setYear(book.getYear());
        bookViewHolder.setId(book.getId());
        bookViewHolder.setPublisher(book.getPublisher());
        bookViewHolder.setPersEval(book.getPersonal_evaluation());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int I) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_rv_cat_item, viewGroup, false);

        return new BookViewHolder(v, this);
    }

    public void updateList() {
        bookList = bookData.getAllBooks();
    }


    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title;
        private TextView author;
        private TextView year;
        private TextView publisher;
        private TextView persEval;
        private long bookId;
        private ListCategoryAdapter adapter;

        public BookViewHolder(View v, ListCategoryAdapter adapter) {
            super(v);
            this.adapter = adapter;
            v.setOnClickListener(this);
            v.setLongClickable(true);
            v.setOnLongClickListener(this);

            title = (TextView) v.findViewById(R.id.book_title);
            author = (TextView) v.findViewById(R.id.book_author);
            year = (TextView) v.findViewById(R.id.book_year);
            publisher = (TextView) v.findViewById(R.id.book_publisher);
            persEval = (TextView) v.findViewById(R.id.book_persEval);

        }

        public void setTitle(String t) {
            title.setText(t);
        }

        public String getTitle() {
            return String.valueOf(title.getText());
        }

        public void setAuthor(String t) {
            author.setText(t);
        }

        public void setYear(Integer t) {
            year.setText("("+String.valueOf(t)+")");
        }

        public void setPersEval(String t) {
            persEval.setText(t);
        }

        public void setPublisher(String t) {
            publisher.setText(t);
        }

        @Override
        public void onClick(View v) {
            Log.v("ITEM", "onClick " + getTitle() + "!");
        }

        @Override
        public boolean onLongClick(View v) {
            ((AppCompatActivity)v.getContext()).getSupportFragmentManager();

            FragmentTransaction ft = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
            Fragment prev = ((AppCompatActivity)v.getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = DeleteDialogFragment.newInstance(bookId,
                    new DeleteDialogFragment.DeleteDialogListener() {
                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {

                        }

                        @Override
                        public void onBookDeleted(final Book b) {
                            adapter.updateList();
                            adapter.notifyDataSetChanged();
                            Log.v("test","updating...");
                            fragment.showBookDeletedSnackbar(b);
                        }
                    });
            newFragment.show(ft, "dialog");
            return true;
        }

        public void setId(long id) {
            bookId = id;
        }
    }
}
