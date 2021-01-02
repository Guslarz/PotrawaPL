package potrawa.logic.restaurant;

import potrawa.application.Application;
import potrawa.logic.common.UserDataController;
import potrawa.data.Restaurant;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;

public class RestaurantUserDataController extends UserDataController {

  public RestaurantUserDataController(Connection connection) {
    super(connection);
  }

  public Restaurant getUserData() {
    try {
      String query = Restaurant.query +
              "WHERE identyfikator = USER ";
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();

      return Restaurant.fromResultSet(resultSet);
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public boolean updateDescription(String description) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Zmien_Opis(?);" +
                      "END;",
              Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, description);
      preparedStatement.execute();
      preparedStatement.close();
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
