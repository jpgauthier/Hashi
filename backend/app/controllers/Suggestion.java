package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class Suggestion extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result save() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        } else {
            String text = json.findPath("text").textValue();
            if(text == null) {
                return badRequest("Missing parameter [text]");
            } else {
                models.Suggestion suggestion = new models.Suggestion(text);
                suggestion.save();

                // Android Json lib requires a response...
                JsonNode r = Json.parse("{\"status\":\"ok\"}");
                return ok(r);
            }
        }
    }

}
