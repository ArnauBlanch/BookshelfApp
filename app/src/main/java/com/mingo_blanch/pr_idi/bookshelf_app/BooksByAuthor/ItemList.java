package com.mingo_blanch.pr_idi.bookshelf_app.BooksByAuthor;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;

/**
 * Created by ivan on 02/01/2017.
 */

abstract class ItemList {

    static final int TYPE_HEADER = 0;
    static final int TYPE_ITEM = 1;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    private Book book;

    abstract public int getType();
}
