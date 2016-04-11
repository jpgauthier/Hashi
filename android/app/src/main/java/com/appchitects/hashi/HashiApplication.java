package com.appchitects.hashi;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.appchitects.hashi.core.model.Ingredient;
import com.appchitects.hashi.core.model.SideDish;
import com.appchitects.hashi.ui.location.GPSTracker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HashiApplication extends Application {

    private static HashiApplication instance;
    private RequestQueue requestQueue;
    private List<Ingredient> ingredients;
    private List<SideDish> sideDishes;
    private GPSTracker gpsTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        this.requestQueue = Volley.newRequestQueue(this);
        this.instance = this;
        this.ingredients = new ArrayList<>();
        this.sideDishes = new ArrayList<>();
        this.gpsTracker = new GPSTracker(getApplicationContext());
    }

    public void setIngredients(List<Ingredient> ingredients) {
        Collections.sort(ingredients, new Comparator<Ingredient>() {
            @Override
            public int compare(Ingredient lhs, Ingredient rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });
        this.ingredients = ingredients;
    }

    public void setSideDishes(List<SideDish> sideDishes) {
        Collections.sort(sideDishes, new Comparator<SideDish>() {
            @Override
            public int compare(SideDish lhs, SideDish rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        });
        this.sideDishes = sideDishes;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public List<SideDish> getSideDishes() {
        return this.sideDishes;
    }

    public GPSTracker getGpsTracker(AppCompatActivity activity) {
        this.gpsTracker.setActivity(activity);
        return this.gpsTracker;
    }

    public synchronized static HashiApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
