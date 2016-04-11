package api;

import com.fasterxml.jackson.databind.JsonNode;
import models.Review;
import play.libs.F;
import play.libs.ws.WS;

import java.util.ArrayList;
import java.util.List;

public class GooglePlacesApi {

    private static String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=%s&key=AIzaSyAj9TBXbK7Of88uoP56BoFKxz_SnPw7pbE";

    public static F.Promise<List<Review>> getReviews(String googlePlacesId) {
        return WS.url(String.format(url, googlePlacesId)).setRequestTimeout(1000).get().map(response -> {
            JsonNode data = response.asJson();
            List<Review> reviews = new ArrayList<>();

            for(JsonNode node : data.get("result").get("reviews")) {
                String text = node.get("text").asText();
                String author = node.get("author_name").asText();
                reviews.add(new Review(author, text));
            }

            return reviews;
        }).recover((t) -> new ArrayList<Review>());
    }
}



