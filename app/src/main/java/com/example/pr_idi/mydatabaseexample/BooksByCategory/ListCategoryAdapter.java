package com.example.pr_idi.mydatabaseexample.BooksByCategory;

import android.app.Activity;
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


public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.BookViewHolder> {

    private List<Book> bookList;
    private BookData bookData;

    public ListCategoryAdapter(BookData bookData) {
        this.bookData = bookData;
        bookList = bookData.getAllBooks();
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

        return new BookViewHolder(v);
    }

    public void updateList() {
        bookList = bookData.getAllBooks();
    }

    public Book getItem(int i) {
        return bookList.get(i);
    }

    public void addBook(Book b) {
        bookList.add(b);
        int pos = bookList.indexOf(b);
        notifyItemInserted(pos);
        notifyItemRangeChanged(pos, bookList.size());
    }

    public void addItem() {
        String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
        int nextInt = new Random().nextInt(4);
        Book b = bookData.createBook(newBook[nextInt*2], newBook[nextInt*2 + 1]);
        bookList.add(b);
        int pos = bookList.indexOf(b);
        notifyItemInserted(pos);
        notifyItemRangeChanged(pos, bookList.size());
    }

    public void addItem(String title, String author, Integer year, String publisher, String category, String persEval) {
        Book b = bookData.createBook(title, author, year, publisher, category, persEval);
        bookList.add(b);
        int pos = bookList.indexOf(b);
        notifyItemInserted(pos);
        notifyItemRangeChanged(pos, bookList.size());
    }

    public void deleteBook(int position) {
        bookData.deleteBook(bookList.get(position));
        bookList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bookList.size());
    }


    public static class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView title;
        private TextView author;
        private TextView year;
        private TextView publisher;
        private TextView persEval;
        private long bookId;

        public BookViewHolder(View v) {
            super(v);

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
            DialogFragment newFragment = DeleteDialogFragment.newInstance(bookId);
            newFragment.show(ft, "dialog");
            return true;
        }

        public void setId(long id) {
            bookId = id;
        }
    }
}
