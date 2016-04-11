package models;

import javax.persistence.*;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import play.data.validation.*;
import java.util.List;

@Entity
public class MenuItem extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String name;

    public String imagePath;

    @ManyToOne
    @Constraints.Required
    @JsonManagedReference
    @JsonUnwrapped(prefix = "category_")
    public MenuItemCategory category;

    @ManyToOne(cascade = CascadeType.ALL)
    @Constraints.Required
    @JsonBackReference
    public Restaurant restaurant;

    @ManyToMany
    @Constraints.Required
    @JsonManagedReference
    public List<models.Ingredient> ingredients;

    public MenuItem(String name, MenuItemCategory category, Restaurant restaurant, List<models.Ingredient> ingredients) {
        this.name = name;
        this.category = category;
        this.restaurant = restaurant;
        this.ingredients = ingredients;
    }

    public static Model.Finder<Long, MenuItem> find = new Model.Finder(Long.class, MenuItem.class);
}
