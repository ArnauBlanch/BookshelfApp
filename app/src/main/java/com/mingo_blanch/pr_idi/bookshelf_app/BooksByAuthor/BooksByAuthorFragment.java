package com.mingo_blanch.pr_idi.bookshelf_app.BooksByAuthor;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.MainActivity;
import com.mingo_blanch.pr_idi.bookshelf_app.R;
import com.mingo_blanch.pr_idi.bookshelf_app.SearchableList;
import com.mingo_blanch.pr_idi.bookshelf_app.UpdatableList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by ivan on 29/12/2016.
 */

public class BooksByAuthorFragment extends Fragment implements UpdatableList, SearchableList {
    private BookData bookData;
    private TreeMap<String, ArrayList<Book>> books;

    private BooksByAuthorAdapter mAdapter;
    private List<ItemList> mItems;


    public BooksByAuthorFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.books_by_author_fragment, container, false);

        bookData = new BookData(getActivity());
        bookData.open();
        mItems = new ArrayList<>();

        // Handle screen rotation
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.books_by_author_fragment);
        int orientation = getResources().getConfiguration().orientation;
        Resources r = getResources();
        int portrait = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, r.getDisplayMetrics());
        int landscape = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rl.setPaddingRelative(0, landscape, 0, 0);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            rl.setPaddingRelative(0, portrait, 0, 0);
        }

        // Prepare the data for the adapter
        prepareList();
        mAdapter = new BooksByAuthorAdapter(mItems);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.books_by_author_rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().
                getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

        // Show the add fab button
        getActivity().findViewById(R.id.fab_btn_create).setVisibility(View.VISIBLE);

        // Set action bar title
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.books_by_author_app_bar_title));

        return view;
    }

    @Override
    public void onDestroyView() {
        getActivity().findViewById(R.id.fab_btn_create).setVisibility(View.GONE);
        super.onDestroyView();
    }

    private void prepareList() {
        books = bookData.getBooksByAuthor();
        setmItems();
    }

    private void setmItems() {
        mItems.clear();
        for (String headerText : books.keySet()) {
            ItemHeader header = new ItemHeader();
            header.setText(headerText);
            mItems.add(header);
            for (Book book : books.get(headerText)) {
                ItemBook item = new ItemBook();
                item.setBook(book);
                mItems.add(item);
            }
        }
    }

    @Override
    public void filter(String text) {
        mItems.clear();
        text = text.toLowerCase();
        for (String headerText : books.keySet()) {
            ArrayList<Book> searchedBookList = new ArrayList<>();
            for (Book b : books.get(headerText)) {
                if (b.getTitle().toLowerCase().contains(text)) searchedBookList.add(b);
            }
            if (searchedBookList.size() > 0) {
                ItemHeader header = new ItemHeader();
                header.setText(headerText);
                mItems.add(header);
                for (Book book : searchedBookList) {
                    ItemBook item = new ItemBook();
                    item.setBook(book);
                    mItems.add(item);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateList() {
        prepareList();
        mAdapter.notifyDataSetChanged();
    }
}
