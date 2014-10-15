package com.skoushan.nyt.models;

public class Multimedia {
    public String url, format;

    public static Multimedia fromTimesSearchMultimedia(TimesSearchArticle.Multimedia timesMultimedia) {
        Multimedia m = new Multimedia();
        m.url = "http://www.nytimes.com/" + timesMultimedia.url;
        m.format = timesMultimedia.subtype.equals("thumbnail") ? "Standard Thumbnail" : "other";
        return m;
    }
}

