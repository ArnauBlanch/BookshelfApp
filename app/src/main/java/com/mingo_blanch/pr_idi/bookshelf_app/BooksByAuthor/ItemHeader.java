package com.mingo_blanch.pr_idi.bookshelf_app.BooksByAuthor;

/**
 * Created by ivan on 02/01/2017.
 */

class ItemHeader extends ItemList {

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
