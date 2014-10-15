package com.skoushan.nyt;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArticleListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private ArticleListAdapter adapter;
    private Queue<DoneInflatingListener> doneInflatingListeners = new LinkedList<DoneInflatingListener>();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ArticleListFragment newInstance(String section) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, section);
        fragment.setArguments(args);
        return fragment;
    }

    public ArticleListFragment() {

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ArticleView.newInstance(getActivity(), adapter.getItem(position).url, adapter.getItem(position).title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            Server.get().listArticles(getArguments().getString(ARG_SECTION_NUMBER), new Callback<List<Article>>() {
                @Override
                public void success(List<Article> retrievedArticles, Response response) {
                    articles.clear();
                    for (Article article : retrievedArticles) {
                        articles.add(article);
                    }
                    notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
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
                convertView = inflater.inflate(R.layout.list_item_article, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.articleTitle = (TextView) convertView.findViewById(R.id.article_title);
                viewHolder.articleImage = (ImageView) convertView.findViewById(R.id.article_image);
                viewHolder.articleAbstract = (TextView) convertView.findViewById(R.id.article_abstract);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Article article = getItem(position);

            String url = null;
            for (int i = 0; i < article.multimedia.length; i++) {
                if (article.multimedia[i].format.equals("Standard Thumbnail")) {
                    url = article.multimedia[i].url;
                    break;
                }
            }
            if (url != null) {
                viewHolder.articleImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(url).into(viewHolder.articleImage);
            } else {
                viewHolder.articleImage.setVisibility(View.GONE);
            }

            viewHolder.articleAbstract.setText(article.updated_date);

            viewHolder.articleTitle.setText(Html.fromHtml(article.title));
            return convertView;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getString(ARG_SECTION_NUMBER));
    }

    @Override
    public void onRefresh() {
        adapter.refresh();
    }
}
