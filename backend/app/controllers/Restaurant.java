package controllers;

import api.GooglePlacesApi;
import com.avaje.ebean.Ebean;
import models.MenuItemCategoryType;
import models.Review;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.F.Promise;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Restaurant extends Controller {

    Comparator<models.Restaurant> byDistance = (r1, r2) -> r1.distance.compareTo(r2.distance);
    Comparator<models.Restaurant> byRating = (r1, r2) -> r2.distance.compareTo(r1.distance);
    Comparator<models.Restaurant> byMatches = (r1, r2) -> {
        Integer r1Matches = r1.ingredientsMatches.size() + r1.sideDishesMatches.size();
        Integer r2Matches = r2.ingredientsMatches.size() + r2.sideDishesMatches.size();
        return r2Matches.compareTo(r1Matches);
    };

    public Result all(Double lat, Double lng, List<String> ing, List<String> side, Integer radius) {
        // TODO: Delete it after we don't need debug
        List<models.Restaurant> restaurants = models.Restaurant.find.all();

        if (lat == null || lng == null) {
            return ok(Json.toJson(restaurants));
        }

        if (lat.isNaN() || lng.isNaN()) {
            return badRequest("Invalid latitude or longitude");
        }

        // TODO: This will be our bottleneck, we need to find the distances using SQL
        restaurants = restaurants.stream()
            .filter(r -> {
                r.distance = distance(lat, lng, r.latitude, r.longitude);
                return r.distance <= (double) radius;
            })
            .map(r -> {
                r.ingredientsMatches = ing.isEmpty() ? new ArrayList<>() : getIngredients(r, ing);
                r.sideDishesMatches = side.isEmpty() ? new ArrayList<>() : getSideDishes(r, side);
                return r;
            })
            .sorted(byMatches.thenComparing(byDistance).thenComparing(byRating))
            .collect(Collectors.toList());

        return ok(Json.toJson(restaurants));
    }

    private List<models.Ingredient> getIngredients(models.Restaurant restaurant, List<String> ingredients) {
        return restaurant.menuItems.stream()
            .flatMap(mi -> mi.ingredients.stream())
            .distinct()
            .filter(i -> ingredients.contains(i.name))
            .collect(Collectors.toList());
    }

    private List<models.MenuItemCategory> getSideDishes(models.Restaurant restaurant, List<String> sideDishes) {
        return restaurant.menuItems.stream()
                .map(mi -> mi.category)
                .distinct()
                .filter(mic -> mic.type.equals(MenuItemCategoryType.SIDE_DISH))
                .filter(mic -> sideDishes.contains(mic.name))
                .collect(Collectors.toList());
    }

    public Promise<Result> reviews(Long restaurantId) {
        models.Restaurant restaurant = Ebean.find(models.Restaurant.class, restaurantId);

        Promise<List<Review>> google = GooglePlacesApi.getReviews(restaurant.googleId);

        return google.map(reviews -> ok(Json.toJson(Stream.concat(reviews.stream(), restaurant.reviews.stream()).collect(Collectors.toList()))));
    }

    public Result menu(Long restaurantId) {
        models.Restaurant restaurant = Ebean.find(models.Restaurant.class, restaurantId);
        return ok(Json.toJson(restaurant.menuItems));
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // Always going to return value in km
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 111.18957696;
        return Math.round(dist * 100) / 100d;
    }
}
