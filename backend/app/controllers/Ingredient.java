package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Ingredient extends Controller {

    public Result all() {
        List<models.Ingredient> restaurants = models.Ingredient.find.all();
        return ok(Json.toJson(restaurants));
    }

}
