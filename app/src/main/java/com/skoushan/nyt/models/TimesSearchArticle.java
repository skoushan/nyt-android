package com.skoushan.nyt.models;

public class TimesSearchArticle {
    public String _id, web_url, pub_date;

    public Headline headline;
    public Multimedia[] multimedia;

    public class Headline {
        public String main;
    }

    public class Multimedia {
        String subtype, url;
    }
}
