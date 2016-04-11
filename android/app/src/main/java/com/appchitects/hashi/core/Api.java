package com.appchitects.hashi.core;

import android.location.Location;
import android.net.Uri;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appchitects.hashi.HashiApplication;
import com.appchitects.hashi.core.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class Api {

    private static String hostname = "http://hashi.elasticbeanstalk.com";

    private Api() {}

    public static void getRestaurants(Response.Listener<JSONArray> okListener, Response.ErrorListener errorListener, Location location) {

        if (location == null) {
            errorListener.onErrorResponse(new VolleyError("Unable to get location"));
        }

        Uri.Builder builder = Uri.parse(hostname).buildUpon();
        builder.appendPath("restaurants")
            .appendQueryParameter("lat", Double.toString(location.getLatitude()))
            .appendQueryParameter("lng", Double.toString(location.getLongitude()));

        JsonArrayRequest r = new JsonArrayRequest(builder.build().toString(), okListener, errorListener);
        HashiApplication.getInstance().getRequestQueue().add(r);
    }

    public static void getRestaurants(Response.Listener<JSONArray> okListener, Response.ErrorListener errorListener, Location location, List<Ingredient> ingredients, List<SideDish> sideDishes, int radius) {

        if (location == null || ingredients == null) {
            errorListener.onErrorResponse(new VolleyError("Unable to get location"));
        }

        Uri.Builder builder = Uri.parse(hostname).buildUpon();
        builder.appendPath("restaurants")
            .appendQueryParameter("lat", Double.toString(location.getLatitude()))
            .appendQueryParameter("lng", Double.toString(location.getLongitude()))
            .appendQueryParameter("radius", Integer.toString(radius));

        for (Ingredient ing : ingredients) {
            builder.appendQueryParameter("ing", ing.name);
        }

        for (SideDish side : sideDishes) {
            builder.appendQueryParameter("side", side.name);
        }

        JsonArrayRequest r = new JsonArrayRequest(builder.build().toString(), okListener, errorListener);
        HashiApplication.getInstance().getRequestQueue().add(r);
    }

    public static void getIngredients(Response.ErrorListener errorListener) {
        Uri.Builder builder = Uri.parse(hostname).buildUpon();
        builder.appendPath("ingredients");

        JsonArrayRequest r = new JsonArrayRequest(builder.build().toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Ingredient> ingredients = parseIngredients(response);

                // Save the ingredients on global application
                HashiApplication.getInstance().setIngredients(ingredients);
            }
        }, errorListener);
        HashiApplication.getInstance().getRequestQueue().add(r);
    }

    public static void getSideDishes(Response.ErrorListener errorListener) {
        Uri.Builder builder = Uri.parse(hostname).buildUpon();
        builder.appendPath("sidedishes");

        JsonArrayRequest r = new JsonArrayRequest(builder.build().toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<SideDish> sideDishes = parseSideDishes(response);

                // Save the side dishes on global application
                HashiApplication.getInstance().setSideDishes(sideDishes);
            }
        }, errorListener);
        HashiApplication.getInstance().getRequestQueue().add(r);
    }

    public static void getMenu(Response.Listener<JSONArray> okListener, Response.ErrorListener errorListener, Restaurant restaurant) {
        Uri.Builder builder = Uri.parse(hostname).buildUpon();
        builder.appendPath("restaurant")
            .appendPath(Long.toString(restaurant.id))
            .appendPath("menu");

        JsonArrayRequest r = new JsonArrayRequest(builder.build().toString(), okListener, errorListener);
        HashiApplication.getInstance().getRequestQueue().add(r);
    }

    public static void getReviews(Response.Listener<JSONArray> okListener, Response.ErrorListener errorListener, Restaurant restaurant) {
        Uri.Builder builder = Uri.parse(hostname).buildUpon();
        builder.appendPath("restaurant")
                .appendPath(Long.toString(restaurant.id))
                .appendPath("reviews");

        JsonArrayRequest r = new JsonArrayRequest(builder.build().toString(), okListener, errorListener);
        HashiApplication.getInstance().getRequestQueue().add(r);
    }

    public static void postSuggestion(Response.Listener<JSONObject> okListener, Response.ErrorListener errorListener, String text) {
        Uri.Builder builder = Uri.parse(hostname).buildUpon();
            builder.appendPath("suggestion");

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("text", text);
            JsonObjectRequest r = new JsonObjectRequest(builder.build().toString(), jsonBody, okListener, errorListener);
            HashiApplication.getInstance().getRequestQueue().add(r);
        } catch (JSONException e) {
            Log.e("Send suggestion API", e.toString());
        }
    }

    // JAVA SUCKS can't have a generic method...
    // It's needs to call the constructor of the generic type and Java doesn't seem
    // to do it other than reflexion
    public static List<Restaurant> parseRestaurants(JSONArray restaurants) {
        List<Restaurant> list = new ArrayList<>();
        for (int i = 0; i < restaurants.length(); i++) {
            try {
                JSONObject jsonRestaurant = restaurants.getJSONObject(i);
                Restaurant r = new Restaurant(jsonRestaurant);
                list.add(r);
            } catch (JSONException e) {
                Log.e("Restaurant API parser", e.toString());
            }
        }

        return list;
    }

    public static List<Review> parseReviews(JSONArray reviews) {
        List<Review> list = new ArrayList<>();
        for (int i = 0; i < reviews.length(); i++) {
            try {
                JSONObject jsonReview = reviews.getJSONObject(i);
                Review r = new Review(jsonReview);
                list.add(r);
            } catch (JSONException e) {
                Log.e("Review API parser", e.toString());
            }
        }
        return list;
    }

    public static List<MenuItem> parseMenu(JSONArray menu) {
        List<MenuItem> list = new ArrayList<>();
        for (int i = 0; i < menu.length(); i++) {
            try {
                JSONObject jsonMenuItem = menu.getJSONObject(i);
                MenuItem mi = new MenuItem(jsonMenuItem);
                list.add(mi);
            } catch (JSONException e) {
                Log.e("Menu API parser", e.toString());
            }
        }
        return list;
    }

    public static List<Ingredient> parseIngredients(JSONArray ingredients) {
        List<Ingredient> list = new ArrayList<>();
        for (int i = 0; i < ingredients.length(); i++) {
            try {
                JSONObject jsonIngredient = ingredients.getJSONObject(i);
                Ingredient ing = new Ingredient(jsonIngredient);
                list.add(ing);
            } catch (JSONException e) {
                Log.e("Ingredient API parser", e.toString());
            }
        }
        return list;
    }

    public static List<SideDish> parseSideDishes(JSONArray sideDishes) {
        List<SideDish> list = new ArrayList<>();
        for (int i = 0; i < sideDishes.length(); i++) {
            try {
                JSONObject jsonSideDish = sideDishes.getJSONObject(i);
                SideDish sideDish = new SideDish(jsonSideDish);
                list.add(sideDish);
            } catch (JSONException e) {
                Log.e("Side Dish API parser", e.toString());
            }
        }
        return list;
    }

    public static String getMediaUrl(String path) {
        if (path != null && path.startsWith("/media")) {
            return hostname + path;
        } else {
            return path;
        }
    }
}
