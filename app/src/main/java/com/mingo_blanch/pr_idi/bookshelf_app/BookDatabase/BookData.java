package com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class BookData {

    private static BookData instance = null;

    // Database fields
    private SQLiteDatabase database;

    // Helper to manipulate table
    private MySQLiteHelper dbHelper;

    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE,
            MySQLiteHelper.COLUMN_AUTHOR,
            MySQLiteHelper.COLUMN_PUBLISHER,
            MySQLiteHelper.COLUMN_YEAR,
            MySQLiteHelper.COLUMN_CATEGORY,
            MySQLiteHelper.COLUMN_PERSONAL_EVALUATION
    };

    public BookData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public static BookData getInstance(Context context) {
        if (instance == null)
            instance = new BookData(context);

        return instance;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Crea nou llibre amb totes les seves dades
    public Book createBook(String title, String author, Integer year, String publisher, String category, String persEval) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_AUTHOR, author);
        values.put(MySQLiteHelper.COLUMN_YEAR, year);
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, publisher);
        values.put(MySQLiteHelper.COLUMN_CATEGORY, category);
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, persEval);

        long insertId = database.insert(MySQLiteHelper.TABLE_BOOKS, null,
                values);

        return getBookById(insertId);
    }

    public void addBook(Book b) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.COLUMN_TITLE, b.getTitle());
        values.put(MySQLiteHelper.COLUMN_AUTHOR, b.getAuthor());
        values.put(MySQLiteHelper.COLUMN_YEAR, b.getYear());
        values.put(MySQLiteHelper.COLUMN_PUBLISHER, b.getPublisher());
        values.put(MySQLiteHelper.COLUMN_CATEGORY, b.getCategory());
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, b.getPersonal_evaluation());

        database.insert(MySQLiteHelper.TABLE_BOOKS, null, values);
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = cursorToBook(cursor);
            books.add(book);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return books;
    }


    private Book cursorToBook(Cursor cursor) {
        Book book = new Book();
        book.setId(cursor.getLong(0));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
        book.setPublisher(cursor.getString(3));
        book.setYear(cursor.getInt(4));
        book.setCategory(cursor.getString(5));
        book.setPersonal_evaluation(cursor.getString(6));
        return book;
    }

    public void deleteBookById(long id) {
        database.delete(MySQLiteHelper.TABLE_BOOKS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public void updatePersonalEvaluation(long id, String persEval) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, persEval);
        database.update(MySQLiteHelper.TABLE_BOOKS,
                values,
                MySQLiteHelper.COLUMN_ID + " = " + id,
                null);
    }

    public Book getBookById(long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Book newBook = cursorToBook(cursor);

        cursor.close();

        return newBook;
    }

    public TreeMap<String, ArrayList<Book>> getBooksByCategory() {
        TreeMap<String, ArrayList<Book>> categories = new TreeMap<>();

        List<Book> books = getAllBooks();

        for (Book book : books) {
            if (!categories.containsKey(book.getCategory()))
                categories.put(book.getCategory(), new ArrayList<Book>());

            categories.get(book.getCategory()).add(book);
        }

        for (TreeMap.Entry<String, ArrayList<Book>> e : categories.entrySet()) {
            Collections.sort(e.getValue());
        }

        return categories;
    }

    public TreeMap<String, ArrayList<Book>> getBooksByAuthor() {
        TreeMap<String, ArrayList<Book>> authors = new TreeMap<>();

        List<Book> books = getAllBooks();

        for (Book book : books) {
            if (!authors.containsKey(book.getAuthor()))
                authors.put(book.getAuthor(), new ArrayList<Book>());

            authors.get(book.getAuthor()).add(book);
        }

        for (TreeMap.Entry<String, ArrayList<Book>> e : authors.entrySet()) {
            Collections.sort(e.getValue());
        }

        return authors;
    }

    private String[] getItemListByColumn(String column) {
        ArrayList<String> items = new ArrayList<>();
        String[] columns = { column };
        Cursor cursor = database.query(true, MySQLiteHelper.TABLE_BOOKS,
                columns, null, null,
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            items.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        String[] itemsArray = new String[items.size()];
        items.toArray(itemsArray);
        return itemsArray;
    }

    public String[] getAuthorList() {
        return getItemListByColumn(MySQLiteHelper.COLUMN_AUTHOR);
    }

    public String[] getCategoryList() {
        return getItemListByColumn(MySQLiteHelper.COLUMN_CATEGORY);
    }

    public String[] getPublisherList() {
        return getItemListByColumn(MySQLiteHelper.COLUMN_PUBLISHER);
    }
}