package com.example.pr_idi.mydatabaseexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

<<<<<<< HEAD
import java.util.List;
import java.util.Random;

/**
 * Created by Arnau on 12/11/16.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private BookData bookData;

    public BookAdapter(BookData bookData) {
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
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int I) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_rv_item, viewGroup, false);

        return new BookViewHolder(v);
    }

    public void updateList() {
        bookList = bookData.getAllBooks();
    }

    public Book getItem(int i) {
        return bookList.get(i);
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

    public void removeItem(int position) {
        bookData.deleteBook(bookList.get(position));
        bookList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bookList.size());
    }


    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView year;

        public BookViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.book_title);
            author = (TextView) v.findViewById(R.id.book_author);
            year = (TextView) v.findViewById(R.id.book_year);
        }

        public void setTitle(String t) {
            title.setText(t);
        }

        public void setAuthor(String t) {
            author.setText(t);
        }

        public void setYear(Integer t) {
            year.setText("("+String.valueOf(t)+")");
        }
=======
import java.util.ArrayList;

/**
 * Created by ivan on 30/12/2016.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private ArrayList<Book> booksList;

    public BookAdapter(ArrayList<Book> booksList) {
        this.booksList = booksList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView book_title, book_author, book_year;

        public ViewHolder(View view) {
            super(view);
            book_title  = (TextView) view.findViewById(R.id.book_title);
            book_author = (TextView) view.findViewById(R.id.book_author);
            book_year   = (TextView) view.findViewById(R.id.book_year);
        }
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_rv_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.book_title.setText(book.getTitle());
        holder.book_author.setText(book.getAuthor());
        holder.book_year.setText(String.valueOf(book.getYear()));
    }

    @Override
    public int getItemCount() {
        return booksList.size();
>>>>>>> part-ivan
    }
}
