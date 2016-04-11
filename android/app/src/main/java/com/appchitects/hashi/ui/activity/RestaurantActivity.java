package com.appchitects.hashi.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.*;
import com.appchitects.hashi.ui.activity.tab.InfoTab;
import com.appchitects.hashi.ui.activity.tab.MatchesTab;
import com.appchitects.hashi.ui.activity.tab.ReviewsTab;
import com.appchitects.hashi.ui.adapter.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

public class RestaurantActivity extends AppCompatActivity {

    private Restaurant restaurant;
    private ViewPager viewPager;
    private InfoTab infoTab;
    private ReviewsTab reviewsTab;
    private MatchesTab matchesTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restaurant = getIntent().getParcelableExtra("restaurant");

        viewPager = (ViewPager) findViewById(R.id.restaurant_viewpager);
        setupViewPager();

        setupToolbar();
        setupHeader();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.restaurant_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    private void setupHeader() {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.restaurant_collapsing_toolbar);
        ImageView image = (ImageView) findViewById(R.id.restaurant_header_image);

        collapsingToolbar.setTitle(restaurant.name);

        Picasso.with(this)
            .load(Api.getMediaUrl(restaurant.imagePath))
            .error(R.drawable.image_default_white)
            .fit()
            .into(image);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        infoTab = new InfoTab();
        reviewsTab = new ReviewsTab();
        matchesTab = new MatchesTab();

        adapter.addFrag(infoTab, "Info");
        if ((restaurant.ingredientsMatches != null && !restaurant.ingredientsMatches.isEmpty()) || (restaurant.sideDishesMatches != null && !restaurant.sideDishesMatches.isEmpty())) {
            adapter.addFrag(matchesTab, "Matches");
        }
        adapter.addFrag(reviewsTab, "Reviews");

        viewPager.setAdapter(adapter);
    }

    /* Info tab listeners */
    public void getDirections(View v) {
        // TODO: Need to test on real device
        String uri = "geo:0,0?q=" + restaurant.address + ", " + restaurant.city;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);

        try {
            startActivity(intent);
        } catch(ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch(ActivityNotFoundException innerEx) {
                Toast.makeText(getApplicationContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void phoneCall(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurant.phone));
        startActivity(intent);
    }

    public void viewMenu(View v) {
        Intent intent = new Intent(getApplicationContext(), MenuCategoryActivity.class);
        intent.putExtra("restaurant", restaurant);
        startActivity(intent);
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    public boolean onOptionsItemSelected(MenuItem item) {
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