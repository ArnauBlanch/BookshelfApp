package com.mingo_blanch.pr_idi.bookshelf_app.BooksByCategory;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.BookData;
import com.mingo_blanch.pr_idi.bookshelf_app.MainActivity;
import com.mingo_blanch.pr_idi.bookshelf_app.R;
import com.mingo_blanch.pr_idi.bookshelf_app.SearchableList;
import com.mingo_blanch.pr_idi.bookshelf_app.UpdatableList;

public class BooksByCategoryFragment extends Fragment implements UpdatableList, SearchableList {

    private BookData bookData;
    private BookByCategoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);

        bookData = BookData.getInstance(getActivity().getApplicationContext());
        bookData.open();

        getActivity().findViewById(R.id.fab_btn_create).setVisibility(View.VISIBLE);

        ///////////////
        // Handle screen rotation
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.booklayout);
        int orientation = getResources().getConfiguration().orientation;
        Resources r = getResources();
        int portrait = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, r.getDisplayMetrics());
        int landscape = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rl.setPaddingRelative(0, landscape, 0, 0);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            rl.setPaddingRelative(0, portrait, 0, 0);
        }

        mAdapter = new BookByCategoryAdapter(bookData.getBooksByCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        // Set action bar title
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.books_by_category_app_bar_title));

        return view;
    }

    @Override
    public void onDestroyView() {
        getActivity().findViewById(R.id.fab_btn_create).setVisibility(View.GONE);
        super.onDestroyView();
    }

    @Override
    public void updateList() {
        mAdapter.updateList(bookData.getBooksByCategory());
    }


    @Override
    public void filter(String text) {
        mAdapter.filter(text);
    }
}
