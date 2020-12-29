package potrawa.logic.client;

import potrawa.data.Opinion;
import potrawa.data.Restaurant;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRestaurantsListController {

  private final Connection connection_;

  public ClientRestaurantsListController(Connection connection) {
    connection_ = connection;
  }

  public List<Restaurant> getRestaurants() {
    try {
      String query = Restaurant.query;
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<Restaurant> restaurants = new ArrayList<>();
      while (resultSet.next()) {
        restaurants.add(Restaurant.fromResultSet(resultSet));
      }
      resultSet.close();
      statement.close();

      return restaurants;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public List<Restaurant> getRestaurants(String searchString) {
    try {
      String query = Restaurant.query +
          "WHERE UPPER(nazwa) LIKE '%' || ? || '%' " +
          "OR UPPER(opis) LIKE '%' || ? || '%' ";
      PreparedStatement statement = connection_.prepareStatement(query);
      searchString = searchString.toUpperCase();
      statement.setString(1, searchString);
      statement.setString(2, searchString);
      ResultSet resultSet = statement.executeQuery();

      List<Restaurant> restaurants = new ArrayList<>();
      while (resultSet.next()) {
        restaurants.add(Restaurant.fromResultSet(resultSet));
      }
      resultSet.close();
      statement.close();

      return restaurants;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public Opinion getOpinion(String restaurantId) {
    try {
      String query = Opinion.query +
          "WHERE identyfikator_restauracji = ? ";
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, restaurantId);
      ResultSet resultSet = statement.executeQuery();

      Opinion opinion = resultSet.next() ? Opinion.fromResultSet(resultSet) : null;
      resultSet.close();
      statement.close();

      return opinion;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }
}
