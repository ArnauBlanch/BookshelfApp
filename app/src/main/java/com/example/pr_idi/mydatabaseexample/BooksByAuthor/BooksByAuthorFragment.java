package com.example.pr_idi.mydatabaseexample.BooksByAuthor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pr_idi.mydatabaseexample.Book;
import com.example.pr_idi.mydatabaseexample.BookData;
import com.example.pr_idi.mydatabaseexample.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ivan on 29/12/2016.
 */

public class BooksByAuthorFragment extends Fragment {
    private BookData bookData;
    private ArrayList<Book> booksList;
    private HashMap<String, List<Book>> booksMap;

    private RecyclerView mRecyclerView;
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
        booksList = (ArrayList<Book>) bookData.getAllBooks();

        sortBooksByTitle();
        groupBooksByAuthor();
        setmItems();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.books_by_author_rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().
                getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new BooksByAuthorAdapter(mItems);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void filter(String text) {
        mItems = new ArrayList<>();
        text = text.toLowerCase();
        for (String headerText : booksMap.keySet()) {
            ArrayList<Book> searchedBookList = new ArrayList<>();
            for (Book b : booksMap.get(headerText)) {
                if (b.getTitle().toLowerCase().contains(text)) searchedBookList.add(b);
            }
            if (searchedBookList.size() > 0) {
                ItemHeader header = new ItemHeader();
                header.setText(headerText);
                mItems.add(header);

                boolean first = true;
                int i = 1;
                for (Book book : searchedBookList) {
                    ItemList item;
                    if (searchedBookList.size() == 1) {
                        // Alone item in section
                        item = new ItemAlone();
                    } else if (i == searchedBookList.size()) {
                        // Last item in section
                        item = new ItemBottom();
                    } else if (first) {
                        // First item in section
                        item = new ItemTop();
                        first = false;
                    } else {
                        // Middle item in section
                        item = new ItemMiddle();
                    }
                    item.setBook(book);
                    mItems.add(item);
                    i++;
                }
            }
        }
        updateAdapter();
    }

    private void updateAdapter() {
        mAdapter = new BooksByAuthorAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateBooks() {
        booksList = (ArrayList<Book>) bookData.getAllBooks();
        sortBooksByTitle();
        groupBooksByAuthor();
        setmItems();
        mAdapter.notifyDataSetChanged();
    }

    private void sortBooksByTitle() {
        // Sort books list (booksList) by title
        Collections.sort(booksList, new Comparator<Book>() {
            public int compare (Book a, Book b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    private void groupBooksByAuthor() {
        ArrayList<String> authorsList = new ArrayList<>();
        booksMap = new HashMap<String, List<Book>> ();

        for (Book a : booksList) {
            if (!authorsList.contains(a.getAuthor())) {
                authorsList.add(a.getAuthor());
                ArrayList<Book> books = new ArrayList<>();
                for (Book b : booksList) {
                    if (b.getAuthor().equals(a.getAuthor()))
                        books.add(b);
                }
                booksMap.put(a.getAuthor(), books);
            }
        }
    }

    private void setmItems() {
        mItems = new ArrayList<>();
        for (String headerText : booksMap.keySet()) {
            ItemHeader header = new ItemHeader();
            header.setText(headerText);
            mItems.add(header);

            boolean first = true;
            int i = 1;
            for (Book book : booksMap.get(headerText)) {
                ItemList item;
                if (booksMap.get(headerText).size() == 1) {
                    // Alone item in section
                    item = new ItemAlone();
                } else if (i == booksMap.get(headerText).size()) {
                    // Last item in section
                    item = new ItemBottom();
                } else if (first) {
                    // First item in section
                    item = new ItemTop();
                    first = false;
                } else {
                    // Middle item in section
                    item = new ItemMiddle();
                }
                item.setBook(book);
                mItems.add(item);
                i++;
            }
        }
    }
}
