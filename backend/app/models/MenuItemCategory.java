package models;

import javax.persistence.*;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.EnumMapping;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.*;
import java.util.List;

@Entity
public class MenuItemCategory extends Model {
    @Id
    public String name;

    public MenuItemCategoryType type;

    public String imagePath;

    @OneToMany
    @JsonBackReference
    public List<MenuItem> menuItems;

    public MenuItemCategory(String name, MenuItemCategoryType type) {
        this.name = name;
        this.type = type;
    }

    public static Model.Finder<String, MenuItemCategory> find = new Model.Finder(String.class, MenuItemCategory.class);
}
