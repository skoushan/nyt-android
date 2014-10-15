package com.skoushan.nyt.activities;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.skoushan.nyt.ArticleListAdapter;
import com.skoushan.nyt.R;
import com.skoushan.nyt.api.TimesServer;
import com.skoushan.nyt.models.Article;
import com.skoushan.nyt.models.TimesSearchArticle;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchResultsActivity extends ListActivity {

    private ArticleListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        adapter = new ArticleListAdapter(this);
        setListAdapter(adapter);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle("Search for '" + query + "'");
            TimesServer.get().search(query, new Callback<TimesServer.Response>() {
                @Override
                public void success(TimesServer.Response r, Response response) {
                    List<Article> articles = new ArrayList<Article>();
                    for (TimesSearchArticle a : r.docs) {
                        articles.add(Article.fromTimesSearchArticle(a));
                    }
                    adapter.setData(articles);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(SearchResultsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SingleArticleActivity.newInstance(this, adapter.getItem(position).url, adapter.getItem(position).title);
    }
}
