package com.mingo_blanch.pr_idi.bookshelf_app.MainWindow;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.OptionsAlertDialog.OptionsAlertDialog;
import com.mingo_blanch.pr_idi.bookshelf_app.R;

import java.util.ArrayList;

/**
 * Created by ivan on 30/12/2016.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<Book> booksList;

    public MainAdapter(ArrayList<Book> booksList) {
        this.booksList = booksList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
                                    implements View.OnLongClickListener {

        private TextView title, author, year;
        private Long id;

        public ViewHolder(View view) {
            super(view);
            view.setLongClickable(true);
            view.setOnLongClickListener(this);
            title  = (TextView) view.findViewById(R.id.book_title);
            author = (TextView) view.findViewById(R.id.book_author);
            year   = (TextView) view.findViewById(R.id.book_year);
        }

        public void setTitle(String title) { this.title.setText(title); }

        public void setAuthor(String author) { this.author.setText(author); }

        public void setYear(Integer year) { this.year.setText(String.valueOf(year)); }

        public void setId(Long id) { this.id = id; }

        @Override
        public boolean onLongClick(final View view) {
            OptionsAlertDialog dialog = new OptionsAlertDialog(view.getContext(), id);
            dialog.showBuilder();
            return true;
        }
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_rv_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.setTitle(book.getTitle());
        holder.setAuthor(book.getAuthor());
        holder.setYear(book.getYear());
        holder.setId(book.getId());
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }


}
