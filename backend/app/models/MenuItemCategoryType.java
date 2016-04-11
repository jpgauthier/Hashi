package models;

import com.avaje.ebean.annotation.EnumMapping;

@EnumMapping(nameValuePairs="ROLL=R, SIDE_DISH=S")
public enum MenuItemCategoryType {
    ROLL,
    SIDE_DISH;
}
