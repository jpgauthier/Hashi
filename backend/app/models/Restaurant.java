package models;

import javax.persistence.*;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import play.data.validation.*;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Restaurant extends Model {
    @Id
    public Long id;

    @Constraints.Required
    public String name;

    public String logoPath;

    public String imagePath;

    @Constraints.Required
    @Constraints.Min(-90)
    @Constraints.Max(90)
    public Double latitude;

    @Constraints.Required
    @Constraints.Min(-180)
    @Constraints.Max(180)
    public Double longitude;

    @Transient
    public Double distance;

    @Transient
    public List<models.Ingredient> ingredientsMatches;

    @Transient
    public List<models.MenuItemCategory> sideDishesMatches;

    @Constraints.Required
    public String address;

    public String googleId;

    public String phone;

    @ManyToOne
    @Constraints.Required
    @JsonUnwrapped(prefix = "city_")
    public City city;

    public Integer rating;

    @OneToMany
    @JsonManagedReference
    @JsonIgnore
    public List<Review> reviews;

    @OneToMany
    @JsonManagedReference
    @JsonIgnore
    public List<MenuItem> menuItems;

    public Restaurant(String name, Double latitude, Double longitude, String address, City city, String phone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    public static Model.Finder<Long, Restaurant> find = new Model.Finder(Long.class, Restaurant.class);
}
