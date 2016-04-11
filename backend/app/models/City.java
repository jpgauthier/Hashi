package models;

import javax.persistence.*;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;

@Entity
public class City extends Model {
    @Id
    public String name;

    @OneToMany
    @JsonBackReference
    public List<Restaurant> restaurants;

    public City(String name) {
        this.name = name;
    }

    public static Model.Finder<String, City> find = new Model.Finder(String.class, City.class);
}
