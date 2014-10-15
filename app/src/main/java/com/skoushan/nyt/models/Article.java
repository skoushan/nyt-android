package com.skoushan.nyt.models;

public class Article {
    public String id, url, title, updated_date;

    public Multimedia[] multimedia;

    public static Article fromTimesSearchArticle(TimesSearchArticle timesSearchArticle) {
        Article article = new Article();
        article.id = timesSearchArticle._id;
        article.url = timesSearchArticle.web_url;
        article.title = timesSearchArticle.headline.main;
        article.updated_date = timesSearchArticle.pub_date;

        Multimedia[] multimedia = new Multimedia[timesSearchArticle.multimedia.length];
        for (int i = 0; i < multimedia.length; i++) {
            multimedia[i] = Multimedia.fromTimesSearchMultimedia(timesSearchArticle.multimedia[i]);
        }
        article.multimedia = multimedia;
        return article;
    }
}
