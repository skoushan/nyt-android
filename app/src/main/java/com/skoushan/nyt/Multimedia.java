package com.skoushan.nyt;

public class Multimedia {
    String url, format;

    public static Multimedia fromTimesMultimedia(TimesArticle.Multimedia timesMultimedia) {
        Multimedia m = new Multimedia();
        m.url = "http://www.nytimes.com/" + timesMultimedia.url;
        m.format = timesMultimedia.subtype.equals("thumbnail") ? "Standard Thumbnail" : "other";
        return m;
    }
}

