package potrawa.logic.restaurant;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDishController {

  private Connection connection_;

  public RestaurantDishController(Connection connection) {
    connection_ = connection;
  }

  public boolean updateDish(String name, String description, double price, String category) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Zmien_Danie(?, ?, ?, ?); " +
                      "END; ",
              Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, name);
      statement.setString(2, description);
      statement.setDouble(3, price);
      statement.setString(4, category);
      statement.execute();
      statement.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean insertDish(String name, String description, double price, String category) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Wstaw_Danie(?, ?, ?, ?); " +
                      "END; ",
              Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, name);
      statement.setString(2, description);
      statement.setDouble(3, price);
      statement.setString(4, category);
      statement.execute();
      statement.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean insertAllergen(String dishName, String allergenName) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Dodaj_Alergen_Do_Dania(?, ?); " +
                      "END; ",
              Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, dishName);
      statement.setString(2, allergenName);
      statement.execute();
      statement.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean deleteAllergen(String dishName, String allergenName) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Usun_Alergen_Z_Dania(?, ?); " +
                      "END; ",
              Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, dishName);
      statement.setString(2, allergenName);
      statement.execute();
      statement.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public List<String> getCategories() {
    try {
      String query = String.format(
              "SELECT nazwa " +
                      "FROM %s.kategorie ",
              Application.schema
      );
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<String> categories = new ArrayList<>();
      while (resultSet.next()) {
        categories.add(resultSet.getString(1));
      }
      resultSet.close();
      statement.close();

      return categories;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public List<String> getAllergens() {
    try {
      String query = String.format(
              "SELECT nazwa " +
                      "FROM %s.alergeny ",
              Application.schema
      );
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<String> allergens = new ArrayList<>();
      while (resultSet.next()) {
        allergens.add(resultSet.getString(1));
      }
      resultSet.close();
      statement.close();

      return allergens;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public List<String> getDishAllergens(String dishName) {
    try {
      String query = String.format(
              "SELECT nazwa_alergenu " +
                      "FROM %s.alergeny_w_daniach " +
                      "WHERE identyfikator_restauracji = USER " +
                      "AND nazwa_dania = ? ",
              Application.schema
      );

      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, dishName);
      ResultSet resultSet = statement.executeQuery();

      List<String> allergens = new ArrayList<>();
      while (resultSet.next()) {
        allergens.add(resultSet.getString(1));
      }
      resultSet.close();
      statement.close();

      return allergens;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }
}
