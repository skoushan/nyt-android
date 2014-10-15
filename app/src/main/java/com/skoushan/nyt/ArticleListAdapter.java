package com.skoushan.nyt;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArticleListAdapter extends BaseAdapter implements ListAdapter {

    private final Context context;

    List<Article> articles = new ArrayList<Article>();
    String[] urls;

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

    public void setData(List<Article> newArticles) {
        articles.clear();
        for (Article article : newArticles) {
            articles.add(article);
        }

        urls = new String[articles.size()];

        for (int i = 0; i < articles.size(); i++) {
            for (Multimedia m : articles.get(i).multimedia) {
                if (m.format.equals("Standard Thumbnail")) {
                    urls[i] = m.url;
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView articleTitle;
        TextView articleAbstract;
        ImageView articleImage;
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

        if (urls[position] != null) {
            viewHolder.articleImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(urls[position]).into(viewHolder.articleImage);
        } else {
            viewHolder.articleImage.setVisibility(View.GONE);
        }

        viewHolder.articleAbstract.setText(article.updated_date);

        viewHolder.articleTitle.setText(Html.fromHtml(article.title));
        return convertView;
    }
}
