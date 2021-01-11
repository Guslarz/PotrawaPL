package potrawa.data;

import potrawa.application.Application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Dish {

  private String restaurantId_;
  private String name_;
  private String description_;
  private double price_;
  private String category_;
  private String allergens_;

  public static final String query = String.format(
      "SELECT " +
          "d.identyfikator_restauracji AS restaurant_id, " +
          "d.nazwa AS name, " +
          "d.opis AS description, " +
          "d.cena AS price, " +
          "d.nazwa_kategorii AS category, " +
          "(" +
          "   SELECT LISTAGG(nazwa_alergenu, ', ') " +
          "     WITHIN GROUP(ORDER BY nazwa_alergenu) " +
          "   FROM %s.alergeny_w_daniach " +
          "   WHERE identyfikator_restauracji = d.identyfikator_restauracji " +
          "     AND nazwa_dania = d.nazwa " +
          ") AS allergens " +
          "FROM %s.dania d ",
      Application.schema, Application.schema
  );

  public static Dish fromResultSet(ResultSet resultSet) throws SQLException {
    Dish dish = new Dish();
    dish.setRestaurantId(resultSet.getString("restaurant_id"));
    dish.setName(resultSet.getString("name"));
    dish.setDescription(resultSet.getString("description"));
    dish.setPrice(resultSet.getDouble("price"));
    dish.setCategory(resultSet.getString("category"));
    dish.setAllergens(resultSet.getString("allergens"));

    return dish;
  }

  public String getRestaurantId() {
    return restaurantId_;
  }

  public String getName() {
    return name_;
  }

  public String getDescription() {
    return description_;
  }

  public double getPrice() {
    return price_;
  }

  public String getCategory() {
    return category_;
  }

  public String getAllergens() {
    return allergens_;
  }

  public void setRestaurantId(String id) {
    restaurantId_ = id;
  }

  public void setName(String name) {
    name_ = name;
  }

  public void setDescription(String description) {
    description_ = description;
  }

  public void setPrice(double price) {
    price_ = price;
  }

  public void setCategory(String category) {
    category_ = category;
  }

  public void setAllergens(String allergens) {
    allergens_ = allergens;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Dish dish = (Dish) o;
    return Objects.equals(name_, dish.name_);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name_);
  }
}
