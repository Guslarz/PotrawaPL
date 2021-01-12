package potrawa.logic.restaurant;

import potrawa.data.Order;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantOrdersListController {

  private final Connection connection_;

  public RestaurantOrdersListController(Connection connection) {
    connection_ = connection;
  }

  public List<Order> getOrders() {
    try {
      String query = Order.query +
              "WHERE z.identyfikator_restauracji = USER ";

      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<Order> orders = new ArrayList<>();
      while (resultSet.next()) {
        orders.add(Order.fromResultSet(resultSet));
      }

      resultSet.close();
      statement.close();
      return orders;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }
}
