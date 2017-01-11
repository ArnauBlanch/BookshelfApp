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

public class HelpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.help));

        View v = inflater.inflate(R.layout.help_fragment, container, false);
        ((TextView) v.findViewById(R.id.help_text)).setText(Html.fromHtml(getString(R.string.help_text)));
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
