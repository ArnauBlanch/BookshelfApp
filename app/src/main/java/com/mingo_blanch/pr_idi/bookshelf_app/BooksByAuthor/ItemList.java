package com.mingo_blanch.pr_idi.bookshelf_app.BooksByAuthor;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;

/**
 * Created by ivan on 02/01/2017.
 */

public abstract class ItemList {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TOP = 1;
    public static final int TYPE_MIDDLE = 2;
    public static final int TYPE_BOTTOM = 3;
    public static final int TYPE_ALONE = 4;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    private Book book;

    abstract public int getType();
}
