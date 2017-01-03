package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ivan on 29/12/2016.
 */

public class BooksByAuthorFragment extends SearchableFragment {
    private BookData bookData;
    private ArrayList<Book> booksList;
    private ArrayList<String> authorsList;
    private HashMap<String, List<String>> booksMap;
    private ExpandableListView expListView;


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
        //booksCopy = new ArrayList<Book>();
        //booksCopy.addAll(booksList);

        // With RecyclerView
        /*
        rv = (RecyclerView) view.findViewById(R.id.books_by_author_rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //rv.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().
                getApplicationContext());
        rv.setLayoutManager(mLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter
        updateAdapter();
        */

        // With ListView
        /*
        List<Book> values = bookData.getAllBooks();                     // Get all books
        List<String> bookTitles = new ArrayList<String>();
        for (Book a : values) bookTitles.add(a.getTitle());             // Get book titles
        Collections.sort(bookTitles, new Comparator<String>() {         // Sort by title
            public int compare (String a, String b) {
                return a.compareTo(b);
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.content_main_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, bookTitles);
        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);
        */

        // Sort books list (booksList) by author
        Collections.sort(booksList, new Comparator<Book>() {
            public int compare (Book a, Book b) {
                return a.getAuthor().compareTo(b.getAuthor());
            }
        });

        expListView = (ExpandableListView) view.findViewById(R.id.books_by_author_exp_list_view);
        authorsList = new ArrayList<>();
        booksMap = new HashMap<String, List<String>> ();

        for (Book a : booksList) {
            if (!authorsList.contains(a.getAuthor())) {
                authorsList.add(a.getAuthor());
                ArrayList<String> books = new ArrayList<String>();
                for (Book b : booksList) {
                    if (b.getAuthor().equals(a.getAuthor()))
                        books.add(b.getTitle());
                }
                booksMap.put(a.getAuthor(), books);
            }
        }

        Display newDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        expListView.setIndicatorBounds(width-100, width);

        updateAdapter();

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
            }
        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
            }
        });
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void filter(String text) {
        authorsList.clear();
        booksMap.clear();
        text = text.toLowerCase();
        for (Book a : booksList) {
            if (!authorsList.contains(a.getAuthor()) &&
                    ((!text.isEmpty() && a.getAuthor().toLowerCase().contains(text)) ||
                            text.isEmpty())) {
                authorsList.add(a.getAuthor());
                ArrayList<String> books = new ArrayList<String>();
                for (Book b : booksList) {
                    if (b.getAuthor().equals(a.getAuthor()))
                        books.add(b.getTitle());
                }
                booksMap.put(a.getAuthor(), books);
            }
        }
        updateAdapter();
    }

    private void updateAdapter() {
        expListView.setAdapter(new ExpandableListAdapter(getActivity().getApplicationContext(),
                authorsList, booksMap));
    }
}
