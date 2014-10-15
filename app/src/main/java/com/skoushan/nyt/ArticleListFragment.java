package com.skoushan.nyt;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
    private static final String ARG_SECTION_NAME = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ArticleListFragment newInstance(String section) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NAME, section);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getString(ARG_SECTION_NAME));
    }

    @Override
    public void onRefresh() {
        Server.get().listArticles(getArguments().getString(ARG_SECTION_NAME), new Callback<List<Article>>() {
            @Override
            public void success(List<Article> retrievedArticles, Response response) {
                adapter.setData(retrievedArticles);
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
