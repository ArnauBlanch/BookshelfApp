package com.example.pr_idi.mydatabaseexample.BooksByAuthor;

/**
 * Created by ivan on 02/01/2017.
 */

public class ItemHeader extends ItemList {

    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
