package potrawa.logic.restaurant;

import potrawa.application.Application;
import potrawa.data.Dish;
import potrawa.error.DefaultSqlHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDishesListController {

  private final Connection connection_;

  public RestaurantDishesListController(Connection connection) {
    connection_ = connection;
  }

  public List<Dish> getDishes() {
    try {
      String query = Dish.query +
              "WHERE identyfikator_restauracji = USER " +
              "ORDER BY category ";
      PreparedStatement statement = connection_.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();

      List<Dish> dishes = new ArrayList<>();
      while (resultSet.next()) {
        dishes.add(Dish.fromResultSet(resultSet));
      }
      resultSet.close();
      statement.close();

      return dishes;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public boolean deleteDish(String name) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Usun_Danie(?); " +
                      "END; ",
              Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, name);
      statement.execute();
      statement.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

}
