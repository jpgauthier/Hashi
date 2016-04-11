package com.appchitects.hashi.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.Restaurant;
import com.squareup.picasso.Picasso;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    private ArrayList<Restaurant> restaurants;
    private Context context;
    private boolean displayMatches;

    public RestaurantAdapter(Context context, int textViewResourceId, ArrayList<Restaurant> items, boolean displayMatches) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.restaurants = items;
        this.displayMatches = displayMatches;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_restaurant, null);
        }

        Restaurant restaurant = restaurants.get(position);
        TextView restaurantName = (TextView) view.findViewById(R.id.list_item_restaurant_name);
        TextView distance = (TextView) view.findViewById(R.id.list_item_restaurant_distance);
        TextView matches = (TextView) view.findViewById(R.id.list_item_restaurant_nb_matches);
        ImageView logo = (ImageView) view.findViewById(R.id.list_item_restaurant_img_restaurant);
        ImageView rating = (ImageView) view.findViewById(R.id.list_item_restaurant_rating);

        Picasso.with(context)
            .load(Api.getMediaUrl(restaurant.logoPath))
            .error(R.drawable.ic_launcher)
            .fit()
            .into(logo);

        if (restaurant.rating == null) {
            rating.setVisibility(View.INVISIBLE);
        } else if(restaurant.rating / 2f <= 1.5) {
            rating.setImageResource(R.drawable.ic_empty_star);
        } else if (restaurant.rating / 2f <= 3.5) {
            rating.setImageResource(R.drawable.ic_half_star);
        } else {
            rating.setImageResource(R.drawable.ic_full_star);
        }

        restaurantName.setText(restaurant.name);
        distance.setText(restaurant.distance.toString() + " km away");

        if (!displayMatches) {
            matches.setVisibility(View.INVISIBLE);
        } else {
            int nbMatches = restaurant.ingredientsMatches.size() + restaurant.sideDishesMatches.size();
            matches.setText(Integer.toString(nbMatches) + (nbMatches > 1 ? " matches" : " match"));
        }

        return view;
    }

}
