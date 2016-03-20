package com.flipkart.persistence;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import com.flipkart.models.Book;

import java.util.ArrayList;

/**
 * Created by kushal.sharma on 20/03/16.
 */
public class BookContentProviderHelperMethods {

    public static ArrayList<Book> getBookListFromDatabase(Activity mAct) {

        ArrayList<Book> mBookList = new ArrayList<>();
        Uri contentUri = BookContentProvider.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {

                Book book = new Book(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_TITLE)),
                        c.getString(c.getColumnIndex(DatabaseHelper.KEY_PUBLISH_DATE)));

                mBookList.add(book);
            } while (c.moveToNext());
        }
        c.close();
        return mBookList;
    }

    public static boolean isBookInDatabase(Activity mAct, int id) {

        ArrayList<Book> list = new ArrayList<>(BookContentProviderHelperMethods
                .getBookListFromDatabase(mAct));
        for (Book listItem : list) {
            if (listItem.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public static Book getBookFromDatabase(Activity mAct, int id) {
        Book book = null;
        Uri contentUri = BookContentProvider.CONTENT_URI;
        Cursor c = mAct.getContentResolver().query(contentUri, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                if (id == c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID))) {
                    book = new Book(c.getInt(c.getColumnIndex(DatabaseHelper.KEY_ID)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_TITLE)),
                            c.getString(c.getColumnIndex(DatabaseHelper.KEY_PUBLISH_DATE)));
                    break;
                }

            } while (c.moveToNext());
        }
        c.close();
        return book;
    }
}
