package com.example.personaldiary;

import java.util.Date;

public class Page {
    private int id;
    private String title;
    private Date datetime;
    private String content;
    private int author;

    public Page(int id) {
        this.id = id;
    }

    public Page(int id, String title, Date datetime) {
        this.id = id;
        this.title = title;
        this.datetime = datetime;
    }

    public Page(String title, Date datetime, String content, int author) {
        super();
        this.title = title;
        this.datetime = datetime;
        setContent(content);
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content.replace("\"", "\\\"");
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
