package models;

import javax.persistence.*;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import play.data.validation.*;

@Entity
public class Review extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String author;

    public String authorImagePath;

    @Constraints.Required
    public String text;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public Restaurant restaurant;

    public Review(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public static Model.Finder<Long, Review> find = new Model.Finder(Long.class, Review.class);
}
