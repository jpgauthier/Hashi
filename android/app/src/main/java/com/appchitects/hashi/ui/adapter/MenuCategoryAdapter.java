package com.appchitects.hashi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.appchitects.hashi.R;

import java.util.ArrayList;

public class MenuCategoryAdapter extends ArrayAdapter<String> {
    private ArrayList<String> categories;
    private Context context;
    private int layout;

    public MenuCategoryAdapter(Context context, int layout, ArrayList<String> categories) {
        super(context, layout, categories);
        this.context = context;
        this.layout = layout;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.layout, null);
        }

        String category = categories.get(position);
        TextView menuItemName = (TextView) view.findViewById(R.id.list_item_menu_name);
        menuItemName.setText(category);

        return view;
    }
}
