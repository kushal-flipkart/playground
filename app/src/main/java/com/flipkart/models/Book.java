package com.flipkart.models;

/**
 * Created by kushal.sharma on 20/03/16.
 */

public class Book {
    private int id;
    private String title;
    private String publishDate;

    public Book() {
    }

    public Book(int id, String title, String publishDate) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
    }

    public Book(String title, String publishDate) {
        this.title = title;
        this.publishDate = publishDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
