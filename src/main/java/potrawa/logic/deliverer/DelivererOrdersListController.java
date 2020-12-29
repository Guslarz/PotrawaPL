package potrawa.logic.deliverer;

import potrawa.application.Application;
import potrawa.data.Order;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DelivererOrdersListController {

  private final Connection connection_;

  public DelivererOrdersListController(Connection connection) {
    connection_ = connection;
  }

  public List<Order> getOrders() {
    try {
      String query = Order.query +
          "WHERE z.identyfikator_dostawcy = USER " +
          "AND z.status = 'DOSTAWA'";
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

  public boolean confirmDelivery(int id) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Dostawca.Zakoncz_Zamowienie(?);" +
              "END;", Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setInt(1, id);
      statement.execute();
      statement.close();
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
