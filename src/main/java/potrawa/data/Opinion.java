package potrawa.data;

import potrawa.application.Application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Opinion {

  private String clientId_;
  private String restaurantId_;
  private int rating_;
  private String comment_;
  private String clientName_;
  private String restaurantName_;

  public static final String query = String.format(
      "SELECT " +
          "o.identyfikator_klienta AS client_id, " +
          "o.identyfikator_restauracji AS restaurant_id, " +
          "o.ocena AS rating, " +
          "o.komentarz AS client_comment, " +
          "k.imie || ' ' || k.nazwisko AS client_name, " +
          "r.nazwa AS restaurant_name " +
          "FROM %s.opinie o " +
          "JOIN %s.klienci k " +
          "ON o.identyfikator_klienta = k.identyfikator " +
          "JOIN %s.restauracje r " +
          "ON o.identyfikator_restauracji = r.identyfikator ",
      Application.schema, Application.schema, Application.schema
  );

  public static Opinion fromResultSet(ResultSet resultSet) throws SQLException {
    Opinion opinion = new Opinion();
    opinion.setClientId(resultSet.getString("client_id"));
    opinion.setRestaurantId(resultSet.getString("restaurant_id"));
    opinion.setRating(resultSet.getInt("rating"));
    opinion.setComment(resultSet.getString("client_comment"));
    opinion.setClientName(resultSet.getString("client_name"));
    opinion.setRestaurantName(resultSet.getString("restaurant_name"));

    return opinion;
  }

  public String getClientId() {
    return clientId_;
  }

  public String getRestaurantId() {
    return restaurantId_;
  }

  public int getRating() {
    return rating_;
  }

  public String getComment() {
    return comment_;
  }

  public String getClientName() {
    return clientName_;
  }

  public String getRestaurantName() {
    return restaurantName_;
  }

  public void setClientId(String id) {
    clientId_ = id;
  }

  public void setRestaurantId(String id) {
    restaurantId_ = id;
  }

  public void setRating(int rating) {
    rating_ = rating;
  }

  public void setComment(String comment) {
    comment_ = comment;
  }

  public void setClientName(String name) {
    clientName_ = name;
  }

  public void setRestaurantName(String name) {
    restaurantName_ = name;
  }
}
