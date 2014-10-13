package com.skoushan.nyt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArticleListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private MainActivity mainActivity;
    private SwipeRefreshLayout swipeLayout;
    private ArticleListAdapter adapter;
    private Queue<DoneInflatingListener> doneInflatingListeners = new LinkedList<DoneInflatingListener>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();

        adapter = new ArticleListAdapter(getActivity());
        setListAdapter(adapter);

        doneInflatingListeners.add(new DoneInflatingListener() {
            @Override
            public void done() {
                swipeLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    private interface DoneInflatingListener {
        void done();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_list, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
//        swipeLayout.setColorSchemeResources(
//                R.color.megaphone_red,
//                R.color.tab_grey,
//                R.color.megaphone_red,
//                R.color.tab_grey);

        while (doneInflatingListeners.size() != 0) {
            doneInflatingListeners.element().done();
            doneInflatingListeners.remove();
        }

        return rootView;
    }

    static class ViewHolder {
        TextView articleTitle;
        TextView articleAbstract;
        ImageView articleImage;
    }

    private class ArticleListAdapter extends BaseAdapter implements ListAdapter {

        private final Context context;

        List<Article> articles = new ArrayList<Article>();

        ArticleListAdapter(Context c) {
            this.context = c;
        }

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Article getItem(int i) {
            return articles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return articles.get(i).id.hashCode();
        }

        public void refresh() {
            mainActivity.service.listArticles(new Callback<List<Article>>() {
                @Override
                public void success(List<Article> retrievedArticles, Response response) {
                    articles.clear();
                    for (Article article : retrievedArticles) {
                        articles.add(article);
                        notifyDataSetChanged();
                        swipeLayout.setRefreshing(false);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_broadcast, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.articleTitle = (TextView) convertView.findViewById(R.id.article_title);
                viewHolder.articleImage = (ImageView) convertView.findViewById(R.id.article_image);
                viewHolder.articleAbstract = (TextView) convertView.findViewById(R.id.article_abstract);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.articleTitle.setText(getItem(position).title);
            return convertView;
        }
    }

    @Override
    public void onRefresh() {
        adapter.refresh();
    }
}
