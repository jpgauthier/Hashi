package com.appchitects.hashi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.ISearchable;
import com.appchitects.hashi.core.model.Ingredient;
import com.appchitects.hashi.core.model.MenuItem;
import com.appchitects.hashi.ui.adapter.ISearchableAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuItemActivity extends AppCompatActivity {

    private GridView ingredientsGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);

        Intent intent = getIntent();
        MenuItem menuItem = intent.getParcelableExtra("menuItem");

        setupToolbar(intent.getStringExtra("restaurantName"));

        TextView name = (TextView) findViewById(R.id.menu_item_name);
        TextView category = (TextView) findViewById(R.id.menu_item_category);
        ImageView image = (ImageView) findViewById(R.id.menu_item_img);

        name.setText(menuItem.name);
        category.setText(menuItem.category);
        setupIngredientsGrid(menuItem);

        Picasso.with(this)
            .load(Api.getMediaUrl(menuItem.imagePath))
            .error(R.drawable.image_default)
            .fit()
            .into(image);
    }

    private void setupToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupIngredientsGrid(MenuItem menuItem) {
        ingredientsGrid = (GridView) findViewById(R.id.menu_items_ingredients);
        ingredientsGrid.setAdapter(new ISearchableAdapter(getApplicationContext(), R.layout.item_searchable, new ArrayList<ISearchable>(menuItem.ingredients)));

        ingredientsGrid.setNumColumns(menuItem.ingredients.size());

        // Calculate the width of the GridView
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (menuItem.ingredients.size() * 85 * scale + 0.5f);  // 85dp

        ViewGroup.LayoutParams layoutParams = ingredientsGrid.getLayoutParams();
        layoutParams.width = pixels;
        ingredientsGrid.setLayoutParams(layoutParams);
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
