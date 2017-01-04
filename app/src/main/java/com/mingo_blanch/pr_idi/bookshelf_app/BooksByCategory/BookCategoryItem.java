package com.mingo_blanch.pr_idi.bookshelf_app.BooksByCategory;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;

class BookCategoryItem {
    static final int CATEGORY_TYPE = 0;
    static final int BOOK_TYPE = 1;
    private String mCategory;
    private Book mBook;
    private int mType;

    BookCategoryItem(String category, Book book, int type) {
        this.mCategory = category;
        this.mBook = book;
        this.mType = type;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public Book getBook() {
        return mBook;
    }

    public void setBook(Book book) {
        this.mBook = book;
    }

    int getType() {
        return mType;
    }
}
