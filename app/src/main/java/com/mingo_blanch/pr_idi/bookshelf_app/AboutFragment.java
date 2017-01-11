package com.mingo_blanch.pr_idi.bookshelf_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Arnau on 6/1/17.
 */

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.about));

        View v = inflater.inflate(R.layout.about_fragment, container, false);
        ((TextView) v.findViewById(R.id.about_description)).setText(Html.fromHtml(getString(R.string.app_description)));
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
