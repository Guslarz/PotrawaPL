package potrawa.logic.restaurant;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RestaurantCategoryController {

  private final Connection connection_;

  public RestaurantCategoryController(Connection connection) {
    connection_ = connection;
  }

  public boolean insertCategory(String name) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Wstaw_Kategorie(?); " +
                      "END; ",
              Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, name);
      statement.execute();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
