package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ivan on 31/12/2016.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> authorsList;
    // The key is the author and the value contains a list of his books
    private HashMap<String, List<String>> booksList;

    public ExpandableListAdapter(Context context, List<String> authorsList,
                                 HashMap<String, List<String>> booksList) {
        this.context = context;
        this.authorsList = authorsList;
        this.booksList = booksList;
    }

    @Override
    public int getGroupCount() {
        return authorsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return booksList.get(authorsList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return authorsList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return booksList.get(authorsList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String author = (String) getGroup(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exp_list_group, null);
        }

        TextView authors_list = (TextView) view.findViewById(R.id.list_title);
        authors_list.setText(author);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String item = (String) getChild(i, i1);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exp_list_item, null);
        }

        TextView books_list = (TextView) view.findViewById(R.id.list_item);
        books_list.setText(item);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
