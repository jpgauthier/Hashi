import play.*;
import play.libs.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.avaje.ebean.*;

import models.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class Global extends GlobalSettings {

    public void onStart(Application app) {
        InitialData.insert(app);
    }

    static class InitialData {

        public static void insert(Application app) {

            // Don't add data if some is already there
            if (Ebean.find(Restaurant.class).findRowCount() == 0) {

                // Save data in yaml file
                Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("data.yml");

                Ebean.save(all.get("cities"));
                Ebean.save(all.get("categories"));
                Ebean.save(all.get("ingredients"));
                Ebean.save(all.get("restaurants"));
                Ebean.save(all.get("menuitems"));

                // Save pictures
                File dir = new File("conf/pictures");
                for (String picturePath : dir.list()) {
                    Path path = Paths.get(dir.getAbsolutePath() + "/" + picturePath);
                    try {
                        byte[] data = Files.readAllBytes(path.toAbsolutePath());

                        ImageInputStream iis = ImageIO.createImageInputStream(new FileInputStream(new File(path.toAbsolutePath().toString())));
                        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

                        String contentType = "";

                        while (readers.hasNext()) {
                            ImageReader read = readers.next();
                            contentType = read.getFormatName();
                        }

                        contentType = "image/" + contentType.toLowerCase();

                        Picture picture = new Picture(path.getFileName().toString(), contentType, data);
                        picture.save();

                    } catch(IOException e) {
                        Logger.warn("Unable to load picture on start");
                    }
                }

            }
        }
    }
}
