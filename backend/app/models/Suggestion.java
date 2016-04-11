package models;

import play.data.validation.Constraints;
import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Suggestion extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String text;

    public Suggestion(String text) {
        this.text = text;
    }

    public static Model.Finder<Long, Restaurant> find = new Model.Finder(Long.class, Suggestion.class);
}
