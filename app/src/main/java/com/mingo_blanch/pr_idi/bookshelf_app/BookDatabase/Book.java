package com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase;

import android.support.annotation.NonNull;

public class Book implements Comparable<Book> {

    // Basic book data manipulation class
    // Contains basic information on the book

    private long id;
    private String author;
    private String title;
    private int year;
    private String publisher;
    private String category;
    private String personal_evaluation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title= title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year= year;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher= publisher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPersonal_evaluation() {
        return personal_evaluation;
    }

    public void setPersonal_evaluation(String personal_evaluation) {
        this.personal_evaluation = personal_evaluation;
    }

    // Will be used by the ArrayAdapter in the ListView
    // Note that it only produces the title and the author
    // Extra information should be created by modifying this
    // method or by adding the methods required
    @Override
    public String toString() {
        return String.format("%d: %s - %s - %d - %s - %s - %s", id, title, author, year, publisher, category, personal_evaluation);
    }

    @Override
    public int compareTo(@NonNull Book another) {
        String b1 = this.getTitle();
        String b2 = another.getTitle();

        return b1.compareToIgnoreCase(b2);
    }
}