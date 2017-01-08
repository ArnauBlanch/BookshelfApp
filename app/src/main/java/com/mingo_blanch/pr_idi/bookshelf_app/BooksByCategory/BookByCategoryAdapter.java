package com.mingo_blanch.pr_idi.bookshelf_app.BooksByCategory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.OptionsAlertDialog.OptionsAlertDialog;
import com.mingo_blanch.pr_idi.bookshelf_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.mingo_blanch.pr_idi.bookshelf_app.BooksByCategory.BookCategoryItem.BOOK_TYPE;
import static com.mingo_blanch.pr_idi.bookshelf_app.BooksByCategory.BookCategoryItem.CATEGORY_TYPE;

class BookByCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private TreeMap<String, ArrayList<Book>> booksByCategory;
    private List<BookCategoryItem> mList;

    BookByCategoryAdapter(TreeMap<String, ArrayList<Book>> booksByCategory) {
        this.booksByCategory = booksByCategory;
        this.mList = getAndPrepareBooksByCategory();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CATEGORY_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
                return new CategoryViewHolder(view);
            case BOOK_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_cat_item, parent, false);
                return new BookViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookCategoryItem object = mList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case CATEGORY_TYPE:
                    ((CategoryViewHolder) holder).setCategoryName(object.getCategory());
                    break;
                case BOOK_TYPE:
                    ((BookViewHolder) holder).setBook(object.getBook());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            BookCategoryItem object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    public void filter(String text) {
        mList.clear();
        text = text.toLowerCase();
        for (String category : booksByCategory.keySet()) {
            ArrayList<Book> searchedBookList = new ArrayList<>();
            for (Book b : booksByCategory.get(category)) {
                if (b.getTitle().toLowerCase().contains(text)) searchedBookList.add(b);
            }
            if (searchedBookList.size() > 0) {
                BookCategoryItem categoryItem = new BookCategoryItem(category, null, CATEGORY_TYPE);
                mList.add(categoryItem);
                for (Book book : searchedBookList) {
                    BookCategoryItem bookItem = new BookCategoryItem(null, book, BOOK_TYPE);
                    mList.add(bookItem);
                }
            }
        }
        notifyDataSetChanged();

    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mCategoryName;

        CategoryViewHolder(View itemView) {
            super(itemView);
            mCategoryName = (TextView) itemView.findViewById(R.id.category_header);
        }

        void setCategoryName(String categoryName) {
            this.mCategoryName.setText(categoryName);
        }
    }

    private static class BookViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView title;
        private TextView author;
        private TextView year;
        private TextView publisher;
        private TextView persEval;
        private long id;

        BookViewHolder(View v) {
            super(v);
            v.setLongClickable(true);
            v.setOnLongClickListener(this);

            title = (TextView) v.findViewById(R.id.book_title);
            author = (TextView) v.findViewById(R.id.book_subtitle);
            year = (TextView) v.findViewById(R.id.book_year);
            publisher = (TextView) v.findViewById(R.id.book_publisher);
            persEval = (TextView) v.findViewById(R.id.book_persEval);
        }

        public void setBook(Book book) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            year.setText('(' + String.valueOf(book.getYear()) + ')');
            publisher.setText(book.getPublisher());
            persEval.setText(book.getPersonal_evaluation());
            id = book.getId();
        }

        @Override
        public boolean onLongClick(View v) {
            OptionsAlertDialog dialog = new OptionsAlertDialog(v.getContext(), id);
            dialog.showBuilder();
            return true;
        }
    }
    public void updateList(TreeMap<String, ArrayList<Book>> booksByCategory) {
        this.booksByCategory = booksByCategory;
        this.mList = getAndPrepareBooksByCategory();
        notifyDataSetChanged();
    }


    private List<BookCategoryItem> getAndPrepareBooksByCategory() {
        List<BookCategoryItem> items = new ArrayList<>();

        for (TreeMap.Entry<String, ArrayList<Book>> category : booksByCategory.entrySet()) {
            items.add(new BookCategoryItem(category.getKey(), null, BookCategoryItem.CATEGORY_TYPE));

            ArrayList<Book> booksInCategory = category.getValue();

            for (Book book : booksInCategory) {
                items.add(new BookCategoryItem(null, book, BookCategoryItem.BOOK_TYPE));
            }

        }

        return items;
    }
}