package com.appchitects.hashi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.model.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<MenuItem> {

    private ArrayList<MenuItem> menu;
    private Context context;
    private int layout;

    public MenuAdapter(Context context, int layout, ArrayList<MenuItem> menu) {
        super(context, layout, menu);
        this.context = context;
        this.layout = layout;
        this.menu = menu;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.layout, null);
        }

        MenuItem menuItem = menu.get(position);
        TextView menuItemName = (TextView) view.findViewById(R.id.list_item_menu_name);
        menuItemName.setText(menuItem.name);

        return view;
    }

}
