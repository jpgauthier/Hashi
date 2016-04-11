package com.appchitects.hashi.ui.activity.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.model.ISearchable;
import com.appchitects.hashi.core.model.Restaurant;
import com.appchitects.hashi.ui.activity.RestaurantActivity;
import com.appchitects.hashi.ui.adapter.ISearchableAdapter;

import java.util.ArrayList;


public class MatchesTab extends Fragment {

    private RestaurantActivity parent;
    private Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_restaurant_matches, container, false);

        parent = (RestaurantActivity) getActivity();
        restaurant = parent.getRestaurant();

        GridView matchesGrid = (GridView) view.findViewById(R.id.activity_restaurant_matches_grid);

        ArrayList<ISearchable> matches = new ArrayList<>();
        if (restaurant.ingredientsMatches != null) matches.addAll(restaurant.ingredientsMatches);
        if (restaurant.sideDishesMatches != null) matches.addAll(restaurant.sideDishesMatches);

        matchesGrid.setAdapter(new ISearchableAdapter(view.getContext(), R.layout.item_searchable, matches));

        return view;
    }
}
