package com.mingo_blanch.pr_idi.bookshelf_app.Help;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mingo_blanch.pr_idi.bookshelf_app.MainActivity;
import com.mingo_blanch.pr_idi.bookshelf_app.R;

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

        View view = inflater.inflate(R.layout.help_fragment, container, false);
        ((TextView) view.findViewById(R.id.help_text)).setText(Html.fromHtml(getString(R.string.help_text)));

        // Handle screen rotation
        ScrollView rl = (ScrollView) view.findViewById(R.id.help_scroll_view);
        int orientation = getResources().getConfiguration().orientation;
        Resources r = getResources();
        int portrait = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, r.getDisplayMetrics());
        int landscape = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rl.setPaddingRelative(0, landscape, 0, 0);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            rl.setPaddingRelative(0, portrait, 0, 0);
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
