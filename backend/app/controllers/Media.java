package controllers;

import com.avaje.ebean.Ebean;
import models.Picture;
import play.mvc.Controller;
import play.mvc.Result;

public class Media extends Controller {
    public Result getPicture(String id) {
        Picture picture = Ebean.find(Picture.class, id);

        if (picture == null) {
            return notFound("Picture not found");
        } else {
            response().setHeader(CACHE_CONTROL, "max-age=259200, public");
            return ok(picture.data).as(picture.contentType);
        }
    }
}
