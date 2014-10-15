package com.skoushan.nyt;

public class TimesArticle {
    String _id, web_url, pub_date;

    Headline headline;
    Multimedia[] multimedia;

    public class Headline {
        String main;
    }

    public class Multimedia {
        String subtype, url;
    }
}
