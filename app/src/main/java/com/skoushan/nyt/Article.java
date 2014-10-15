package com.skoushan.nyt;

public class Article {
    String id, url, title, updated_date;

    Multimedia[] multimedia;

    public class Multimedia {
        String url, format, type;
    }
}
