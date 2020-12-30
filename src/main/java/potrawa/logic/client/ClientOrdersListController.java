package potrawa.logic.client;

import potrawa.data.Order;
import potrawa.error.DefaultSqlHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientOrdersListController {

  private final Connection connection_;

  public ClientOrdersListController(Connection connection) {
    connection_ = connection;
  }

  public List<Order> getOrders() {
    try {
      String query = Order.query +
          "WHERE identyfikator_klienta = USER " +
          "ORDER BY timestamp DESC ";
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
