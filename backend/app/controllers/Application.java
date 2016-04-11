package controllers;

import models.Picture;
import models.Restaurant;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;

import java.io.*;
import java.nio.file.Files;

public class Application extends Controller {

    public Result index() {
        return ok(Json.toJson(Restaurant.find.all()));
    }

    public Result uploadGet() {
        return ok(upload.render());
    }

    public Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart fp = body.getFile("picture");

        if (fp == null) {
            return badRequest("Missing file");
        }

        String fileName = fp.getFilename();
        String contentType = fp.getContentType();
        File file = fp.getFile();

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());

            Picture picture = new Picture(fileName, contentType, bytes);
            picture.save();
            return ok("File uploaded");
        } catch (IOException ex) {
            Logger.warn("Unable to cast file to byte array");
            return internalServerError("Unable to process picture, please verify it's a valid file");
        }
    }

}
