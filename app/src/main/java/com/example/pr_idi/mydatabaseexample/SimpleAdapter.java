package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.BookViewHolder> {

    private final BookData bookData;
    private final Context context;
    private List<Book> bookList;

    public SimpleAdapter(BookData bookData, Context context) {
        this.bookData = bookData;
        this.context = context;
//        this.bookList = bookData.getAllBooks();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.setTitle(book.getTitle());
        holder.setAuthor(book.getAuthor());
        holder.setYear(book.getYear());
        /*holder.setPublisher(book.getPublisher());
        holder.setCategory(book.getCategory());
        holder.setPersEval(book.getPersonal_evaluation());*/
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView year;
        /*private TextView publisher;
        private TextView category;
        private TextView persEval;*/

        BookViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.book_title);
            author = (TextView) v.findViewById(R.id.book_author);
            year = (TextView) v.findViewById(R.id.book_year);
            /*publisher = (TextView) v.findViewById(R.id.book_publisher);
            category = (TextView) v.findViewById(R.id.book_category);
            persEval = (TextView) v.findViewById(R.id.book_persEval);*/
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

        /*public void setPublisher(String t) { publisher.setText(t); }

        public void setCategory(String t) { category.setText(t); }

        public void setPersEval(String t) { persEval.setText(t); }*/

    }

    public void add(Book b) {
        bookList.add(b);
        int pos = bookList.indexOf(b);
        notifyItemInserted(pos);
        notifyItemRangeChanged(pos, getItemCount());
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            bookList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }
}
