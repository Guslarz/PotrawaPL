package potrawa.logic.restaurant;

import potrawa.application.Application;
import potrawa.data.Deliverer;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantOrderController {

  private final Connection connection_;

  public RestaurantOrderController (Connection connection) {
    connection_ = connection;
  }

  public boolean updateStatus(int orderId, String delivererId) {
    try {
      String query = String.format(
              "BEGIN " +
                      "%s.Restauracja.Zmien_Status_Zamowienia(?, ?);" +
                      "END;", Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setInt(1, orderId);
      statement.setString(2, delivererId);
      statement.execute();
      statement.close();
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public List<Deliverer> getDeliverers() {
    try {
      String query = Deliverer.query +
              "WHERE czy_dostepny = 'T'";
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<Deliverer> deliverers = new ArrayList<>();
      while (resultSet.next()) {
        deliverers.add(Deliverer.fromResultSet(resultSet));
      }
      resultSet.close();
      statement.close();

      return deliverers;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }
}
