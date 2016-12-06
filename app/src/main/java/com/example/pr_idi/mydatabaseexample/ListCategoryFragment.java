package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListCategoryFragment extends Fragment {

    private BookData bookData;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_rv, container, false);

        bookData = BookData.getInstance(getActivity().getApplicationContext());
        bookData.open();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        rv = (RecyclerView) view.findViewById(R.id.list);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rv.setAdapter(new BookAdapter(bookData));

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyActivity","FAB premut!");
            }
        });

        return view;
    }

}
