package models;

import javax.persistence.*;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.*;
import java.util.List;

@Entity
public class Ingredient extends Model {
    @Id
    public String name;

    public String alternativeName;

    public String imagePath;

    @ManyToMany
    @JsonBackReference
    public List<MenuItem> menuItems;

    public Ingredient(String name) {
        this.name = name;
    }

    public static Model.Finder<String, Ingredient> find = new Model.Finder(String.class, Ingredient.class);
}
