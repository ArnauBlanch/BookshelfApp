package com.example.pr_idi.mydatabaseexample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    }
}
