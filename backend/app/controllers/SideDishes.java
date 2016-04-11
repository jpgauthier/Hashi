package controllers;

import models.MenuItemCategoryType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class SideDishes extends Controller {
    public Result all() {
        List<models.MenuItemCategory> categories = models.MenuItemCategory.find.where().eq("type", MenuItemCategoryType.SIDE_DISH).findList();
        return ok(Json.toJson(categories));
    }
}
