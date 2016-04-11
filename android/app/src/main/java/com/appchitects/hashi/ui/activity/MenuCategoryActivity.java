package com.appchitects.hashi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.MenuItem;
import com.appchitects.hashi.core.model.Restaurant;
import com.appchitects.hashi.ui.adapter.MenuCategoryAdapter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuCategoryActivity extends AppCompatActivity {

    private Restaurant restaurant;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> categories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_categories);

        restaurant = getIntent().getParcelableExtra("restaurant");

        setupToolbar();
        setupList(restaurant);
        fetchData();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Categories");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupList(final Restaurant restaurant) {
        ListView list = (ListView) findViewById(R.id.activity_menu_categories_list);

        categories = restaurant.menu == null ? new ArrayList<String>() : getCategories(restaurant.menu);
        adapter = new MenuCategoryAdapter(this, R.layout.list_item_menu, categories);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                ArrayList<MenuItem> filteredItems = new ArrayList<>();

                for (MenuItem item : restaurant.menu) {
                    if (item.category.equals(selectedCategory)) {
                        filteredItems.add(item);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("restaurant", restaurant);
                intent.putParcelableArrayListExtra("menu", filteredItems);
                startActivity(intent);
            }
        });
    }

    private void fetchData(){
        if (restaurant.menu == null) {
            final ProgressDialog progressDialog = ProgressDialog.show(MenuCategoryActivity.this, "In progress", "Please sit tight!");
            Api.getMenu(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    List<MenuItem> menu = Api.parseMenu(response);
                    restaurant.setMenu(menu);
                    categories = getCategories(menu);
                    adapter.clear();
                    adapter.addAll(categories);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unable to get menu of this restaurant", Toast.LENGTH_LONG).show();
                }
            }, restaurant);
        }
    }

    private ArrayList<String> getCategories(List<MenuItem> menu) {
        Set<String> categoriesSet = new HashSet<>();

        for(MenuItem item : menu) {
            categoriesSet.add(item.category);
        }

        return new ArrayList<>(categoriesSet);
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
