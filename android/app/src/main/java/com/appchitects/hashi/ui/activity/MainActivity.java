package com.appchitects.hashi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appchitects.hashi.HashiApplication;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.Restaurant;
import com.appchitects.hashi.ui.adapter.RestaurantAdapter;
import com.appchitects.hashi.ui.location.GPSTracker;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RestaurantAdapter adapter;
    private List<Restaurant> restaurants;
    private GPSTracker gpsTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        restaurants = getIntent().getParcelableArrayListExtra("restaurants");

        setupList(restaurants);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_search_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        gpsTracker = HashiApplication.getInstance().getGpsTracker(MainActivity.this);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupList(final List<Restaurant> restaurants) {
        ListView restaurantsList = (ListView) findViewById(R.id.main_restaurants);

        adapter = new RestaurantAdapter(this, R.layout.list_item_restaurant, (ArrayList<Restaurant>) restaurants, false);
        restaurantsList.setAdapter(adapter);
        restaurantsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = restaurants.get(position);
                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                intent.putExtra("restaurant", restaurant);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshList();
                return true;
            case R.id.action_suggest:
                intent = new Intent(getBaseContext(), SuggestActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private void refreshList() {
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "In progress", "Please sit tight!");

        Location location = gpsTracker.getLocation();
        Api.getRestaurants(
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonRestaurants) {
                    List<Restaurant> restaurants = Api.parseRestaurants(jsonRestaurants);
                    adapter.clear();
                    adapter.addAll(restaurants);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Unable to get restaurants: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, location);
    }
}