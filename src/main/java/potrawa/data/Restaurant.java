package potrawa.data;

import potrawa.application.Application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Restaurant {

  private String id_;
  private String name_;
  private String description_;
  private double avgRating_;

  public static final String query = String.format(
      "SELECT " +
          "r.identyfikator AS id, " +
          "r.nazwa AS name, " +
          "r.opis AS description, " +
          "(" +
          "   SELECT AVG(ocena) " +
          "   FROM %s.opinie " +
          "   WHERE identyfikator_restauracji = r.identyfikator " +
          ") AS avg_rating " +
          "FROM %s.restauracje r " +
          "ON r.identyfikator = o.identyfikator_restauracji ",
      Application.schema, Application.schema
  );

  public static Restaurant fromResultSet(ResultSet resultSet) throws SQLException {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(resultSet.getString("id"));
    restaurant.setName(resultSet.getString("name"));
    restaurant.setDescription(resultSet.getString("description"));
    restaurant.setAvgRating(resultSet.getDouble("avg_rating"));

    return restaurant;
  }

  public String getId() {
    return id_;
  }

  public String getName() {
    return name_;
  }

  public String getDescription() {
    return description_;
  }

  public double getAvgRating() {
    return avgRating_;
  }

  public void setId(String id) {
    id_ = id;
  }

  public void setName(String name) {
    name_ = name;
  }

  public void setDescription(String description) {
    description_ = description;
  }

  public void setAvgRating(double avgRating) {
    avgRating_ = avgRating;
  }
}
