package com.appchitects.hashi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.ISearchable;
import com.appchitects.hashi.core.model.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ISearchableAdapter extends ArrayAdapter<ISearchable> {
    private Context context;
    private int layout;
    private ArrayList<ISearchable> items;

    public ISearchableAdapter(Context context, int layout, ArrayList<ISearchable> items) {
        super(context, layout, items);
        this.layout = layout;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
        }

        ISearchable item = items.get(position);

        TextView name = (TextView) view.findViewById(R.id.item_searchable_text);
        name.setText(item.getName());

        ImageView img = (ImageView) view.findViewById(R.id.item_searchable_img);

        Picasso.with(context)
            .load(Api.getMediaUrl(item.getImagePath()))
            .error(R.drawable.ingredient_default)
            .fit()
            .into(img);

        return view;
    }
}
