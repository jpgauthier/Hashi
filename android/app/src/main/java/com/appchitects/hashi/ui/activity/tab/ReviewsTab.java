package com.appchitects.hashi.ui.activity.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.Restaurant;
import com.appchitects.hashi.core.model.Review;
import com.appchitects.hashi.ui.activity.RestaurantActivity;
import com.appchitects.hashi.ui.adapter.ReviewAdapter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ReviewsTab extends Fragment {

    private RestaurantActivity parent;
    private ReviewAdapter adapter;
    private Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_restaurant_reviews, container, false);

        parent = (RestaurantActivity) getActivity();
        restaurant = parent.getRestaurant();

        ListView list = (ListView) view.findViewById(R.id.activity_restaurant_reviews_list);

        adapter = new ReviewAdapter(parent.getApplicationContext(),
                R.layout.list_item_review,
                restaurant.reviews == null ? new ArrayList<Review>() : (ArrayList<Review>) restaurant.reviews);

        list.setAdapter(adapter);
        list.setClickable(false);

        fetchData();

        return view;
    }

    private void fetchData() {
        if (restaurant.reviews == null) {
            Api.getReviews(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    List<Review> reviews = Api.parseReviews(response);
                    restaurant.setReviews(reviews);
                    adapter.clear();
                    adapter.addAll(restaurant.reviews);
                    adapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(parent.getApplicationContext(), "Unable to get reviews of this restaurant", Toast.LENGTH_LONG).show();
                }
            }, restaurant);
        }
    }
}
