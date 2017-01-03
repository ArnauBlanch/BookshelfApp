package com.example.pr_idi.mydatabaseexample.BooksByCategory;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pr_idi.mydatabaseexample.Book;
import com.example.pr_idi.mydatabaseexample.BookData;
import com.example.pr_idi.mydatabaseexample.R;

public class ListCategoryFragment extends Fragment {

    private BookData bookData;
    private RecyclerView rv;
    private ListCategoryAdapter rvAdapter;
    private CoordinatorLayout coordinatorLayout;

    public interface ListCategoryFragmentListener {
        void onAddBook();
    }

    private ListCategoryFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);

        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.clayout);

        bookData = BookData.getInstance(getActivity().getApplicationContext());
        bookData.open();

        rv = (RecyclerView) view.findViewById(R.id.list);

        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvAdapter = new ListCategoryAdapter(bookData, this);
        rv.setAdapter(rvAdapter);

        getActivity().findViewById(R.id.fab_btn_create).setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onDestroyView() {
        getActivity().findViewById(R.id.fab_btn_create).setVisibility(View.GONE);
        super.onDestroyView();
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
