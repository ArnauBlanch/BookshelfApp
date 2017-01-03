package com.example.pr_idi.mydatabaseexample.BooksByCategory;

import android.app.Activity;
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

import com.example.pr_idi.mydatabaseexample.BookData;
import com.example.pr_idi.mydatabaseexample.R;

public class ListCategoryFragment extends Fragment {

    private BookData bookData;
    private RecyclerView rv;
    private ListCategoryAdapter rvAdapter;

    public interface ListCategoryFragmentListener {
        void onAddBook();
    }

    private ListCategoryFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);

        bookData = BookData.getInstance(getActivity().getApplicationContext());
        bookData.open();

        rv = (RecyclerView) view.findViewById(R.id.list);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvAdapter = new ListCategoryAdapter(bookData);
        rv.setAdapter(rvAdapter);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyActivity","FAB premut!");
                listener.onAddBook();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ListCategoryFragment.ListCategoryFragmentListener) getActivity();
        Log.v("TEST", "onAttach!!!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void updateBooks() {
        rvAdapter.updateList();
        rvAdapter.notifyDataSetChanged();
    }


}
