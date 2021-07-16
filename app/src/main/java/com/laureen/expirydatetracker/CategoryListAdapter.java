package com.laureen.expirydatetracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private final Activity context;
    private final Integer[] imageId;
    private final List<Category> categories;
    public CategoryListAdapter(Activity context, List<Category> categories, Integer[] imageId) {
        super(context, R.layout.category_row_item, categories);
        this.context = context;
        this.categories = categories;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.category_row_item, null, true);
        TextView cat_title = (TextView) rowView.findViewById(R.id.cat_title);
        ImageView cat_img = (ImageView) rowView.findViewById(R.id.cat_img);
        cat_title.setText(categories.get(position).getName());
        cat_img.setImageResource(imageId[categories.get(position).getImageNo() - 1]);
        return rowView;
    }
}
