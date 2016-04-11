package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Picture extends Model {
    @Id
    public String name;

    @Constraints.Required
    public String contentType;

    @Lob
    @Constraints.Required
    public byte[] data;

    public Picture(String name, String contentType, byte[] data) {
        this.name = name;
        this.contentType = contentType;
        this.data = data;
    }
}
