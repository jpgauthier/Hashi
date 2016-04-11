package com.appchitects.hashi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.model.MenuItem;
import com.appchitects.hashi.core.model.Restaurant;
import com.appchitects.hashi.ui.adapter.MenuAdapter;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private Restaurant restaurant;
    private ArrayList<MenuItem> menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        restaurant = getIntent().getParcelableExtra("restaurant");
        menu = getIntent().getParcelableArrayListExtra("menu");

        setupToolbar();
        setupList(menu);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Items");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupList(final ArrayList<MenuItem> menu) {
        ListView list = (ListView) findViewById(R.id.activity_menu_list);
        list.setAdapter(new MenuAdapter(getApplicationContext(), R.layout.list_item_menu, menu));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MenuItemActivity.class);
                intent.putExtra("restaurant", restaurant);
                intent.putExtra("menuItem", menu.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_small, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_suggest:
                intent = new Intent(getApplicationContext(), SuggestActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}
