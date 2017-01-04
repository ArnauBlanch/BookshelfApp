package com.mingo_blanch.pr_idi.bookshelf_app.MainWindow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.R;
import com.mingo_blanch.pr_idi.bookshelf_app.SearchableList;
import com.mingo_blanch.pr_idi.bookshelf_app.UpdatableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ivan on 29/12/2016.
 */

public class MainFragment extends Fragment implements UpdatableList, SearchableList {
    private BookData bookData;
    private ArrayList<Book> booksList, booksCopy;
    private RecyclerView rv;
    private MainAdapter mAdapter;

    public MainFragment (){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_main, container, false);

        bookData = new BookData(getActivity());
        bookData.open();
        booksList = (ArrayList<Book>) bookData.getAllBooks();
        booksCopy = new ArrayList<Book>();
        booksCopy.addAll(booksList);

        rv = (RecyclerView) view.findViewById(R.id.content_main_rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().
                getApplicationContext());
        rv.setLayoutManager(mLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter
        sortBooksList();
        mAdapter = new MainAdapter(booksList);
        rv.setAdapter(mAdapter);

        // Show the add fab button
        getActivity().findViewById(R.id.fab_btn_create).setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void filter(String text) {
        // Filter booksList with text
        if (!text.isEmpty()) {
            booksList.clear();
            text = text.toLowerCase();
            for (Book book : booksCopy) {
                if (book.getTitle().toLowerCase().contains(text)) {
                    booksList.add(book);
                }
            }
        }
        else {
            booksList.clear();
            booksList.addAll(booksCopy);
        }
        sortBooksList();
        mAdapter.notifyDataSetChanged();
    }

    private void sortBooksList() {
        // Sort by title
        Collections.sort(booksList, new Comparator<Book>() {
            public int compare(Book a, Book b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    @Override
    public void updateList() {
        booksList = (ArrayList<Book>) bookData.getAllBooks();
        sortBooksList();
        mAdapter.notifyDataSetChanged();
    }
}
