package com.skoushan.nyt;

public class Article {
    String id, url, title, updated_date;

    Multimedia[] multimedia;


    public static Article fromTimesArticle(TimesArticle timesArticle) {
        Article article = new Article();
        article.id = timesArticle._id;
        article.url = timesArticle.web_url;
        article.title = timesArticle.headline.main;
        article.updated_date = timesArticle.pub_date;

        Multimedia[] multimedia = new Multimedia[timesArticle.multimedia.length];
        for (int i = 0; i < multimedia.length; i++) {
            multimedia[i] = Multimedia.fromTimesMultimedia(timesArticle.multimedia[i]);
        }
        article.multimedia = multimedia;
        return article;
    }
}
