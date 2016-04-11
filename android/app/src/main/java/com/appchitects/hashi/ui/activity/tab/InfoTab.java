package com.appchitects.hashi.ui.activity.tab;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.model.ISearchable;
import com.appchitects.hashi.core.model.Ingredient;
import com.appchitects.hashi.core.model.Restaurant;
import com.appchitects.hashi.ui.activity.RestaurantActivity;
import com.appchitects.hashi.ui.adapter.ISearchableAdapter;

import java.util.ArrayList;

public class InfoTab extends Fragment {

    private RestaurantActivity parent;
    private Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_restaurant_info, container, false);

        parent = (RestaurantActivity) getActivity();
        restaurant = parent.getRestaurant();

        TextView address = (TextView) view.findViewById(R.id.restaurant_info_address);
        TextView city = (TextView) view.findViewById(R.id.restaurant_info_city);
        TextView phone = (TextView) view.findViewById(R.id.restaurant_info_phone);
        RatingBar rating = (RatingBar) view.findViewById(R.id.restaurant_info_rating);

        address.setText(restaurant.address);
        city.setText(restaurant.city);
        phone.setText(restaurant.phone);

        if (restaurant.rating != null) {
            rating.setRating(restaurant.rating / 2f);
        }

        return view;
    }
}
