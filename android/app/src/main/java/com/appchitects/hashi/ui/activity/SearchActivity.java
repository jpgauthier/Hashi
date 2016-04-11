package com.appchitects.hashi.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appchitects.hashi.HashiApplication;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.*;
import com.appchitects.hashi.ui.adapter.ISearchableAdapter;
import com.appchitects.hashi.ui.location.GPSTracker;
import org.json.JSONArray;

import java.util.*;

public class SearchActivity extends AppCompatActivity {

    private GPSTracker gpsTracker;
    private List<Ingredient> selectedIngredients = new ArrayList<>();
    private List<SideDish> selectedSideDishes = new ArrayList<>();
    private int radius;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        gpsTracker = HashiApplication.getInstance().getGpsTracker(SearchActivity.this);

        setUpIngredientsGrid(HashiApplication.getInstance().getIngredients());
        setUpSidesGrid(HashiApplication.getInstance().getSideDishes());
        setUpDistanceRadius();
        setUpSearchButton();
    }

    private void setUpIngredientsGrid(final List<Ingredient> ingredients) {
        GridView ingredientsGrid = (GridView) findViewById(R.id.search_ingredients);
        ingredientsGrid.setAdapter(new ISearchableAdapter(this, R.layout.item_searchable, new ArrayList<ISearchable>(ingredients)));

        // Calculate the number of columns to have three rows (Ingredients)
        int numberOfColumns = (int) Math.ceil(ingredients.size() / 2d);
        ingredientsGrid.setNumColumns(numberOfColumns);

        // Calculate the width of the GridView (Ingredients)
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (numberOfColumns * 85 * scale + 0.5f);  // 85dp

        ViewGroup.LayoutParams layoutParams = ingredientsGrid.getLayoutParams();
        layoutParams.width = pixels;
        ingredientsGrid.setLayoutParams(layoutParams);

        ingredientsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ingredient ingredient = ingredients.get(position);

                if (selectedIngredients.contains(ingredient)) {
                    selectedIngredients.remove(ingredient);
                    view.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                } else {
                    selectedIngredients.add(ingredient);
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                }
            }
        });
    }

    private void setUpSidesGrid(final List<SideDish> sideDishes) {
        GridView sideDishesGrid = (GridView) findViewById(R.id.search_sides);
        sideDishesGrid.setAdapter(new ISearchableAdapter(this, R.layout.item_searchable,  new ArrayList<ISearchable>(sideDishes)));

        sideDishesGrid.setNumColumns(sideDishes.size());

        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (sideDishes.size() * 85 * scale + 0.5f);  // 85dp

        ViewGroup.LayoutParams layoutParams = sideDishesGrid.getLayoutParams();
        layoutParams.width = pixels;
        sideDishesGrid.setLayoutParams(layoutParams);

        sideDishesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SideDish side = sideDishes.get(position);

                if (selectedSideDishes.contains(side)) {
                    selectedSideDishes.remove(side);
                    view.setBackgroundColor(getResources().getColor(R.color.windowBackground));
                } else {
                    selectedSideDishes.add(side);
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                }
            }
        });
    }

    private void setUpDistanceRadius() {
        SeekBar radiusBar = (SeekBar)findViewById(R.id.search_radius);

        radius = radiusBar.getProgress();
        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Within " + String.valueOf(radius)+" km",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpSearchButton() {

        final Location location = gpsTracker.getLocation();

        Button searchBtn = (Button) findViewById(R.id.search_btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(SearchActivity.this, "In progress", "Please sit tight!");

                Api.getRestaurants(new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Restaurant> restaurants = Api.parseRestaurants(response);
                        Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
                        intent.putParcelableArrayListExtra("restaurants", (ArrayList<Restaurant>) restaurants);
                        intent.putParcelableArrayListExtra("selectedIngredients", (ArrayList<Ingredient>) selectedIngredients);
                        intent.putParcelableArrayListExtra("selectedSideDishes", (ArrayList<SideDish>) selectedSideDishes);
                        intent.putExtra("radius", radius);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Unable to contact server", Toast.LENGTH_LONG).show();
                    }
                }, location, selectedIngredients, selectedSideDishes, radius);
            }
        });
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                intent = new Intent(getBaseContext(), SuggestActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
}