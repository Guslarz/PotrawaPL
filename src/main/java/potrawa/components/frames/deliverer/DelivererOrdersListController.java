package potrawa.components.frames.deliverer;

import potrawa.application.Application;
import potrawa.data.deliverer.DelivererOrder;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DelivererOrdersListController {

  private final Connection connection_;

  public DelivererOrdersListController(Connection connection) {
    connection_ = connection;
  }

  public List<DelivererOrder> getOrders() {
    try {
      String query = String.format(
          "SELECT " +
              "z.id_zamowienia AS id, " +
              "r.nazwa AS restaurant_name, " +
              "z.adres AS address, " +
              "z.nazwa_metody_platnosci AS payment_method, " +
              "(" +
              "    SELECT SUM(d.cena * dwz.licznosc) " +
              "    FROM %s.dania d " +
              "    JOIN %s.dania_w_zamowieniu dwz " +
              "    ON d.IDENTYFIKATOR_RESTAURACJI = dwz.IDENTYFIKATOR_RESTAURACJI " +
              "       AND d.NAZWA = dwz.NAZWA_DANIA " +
              "    WHERE dwz.id_zamowienia = z.id_zamowienia " +
              ") AS price, " +
              "z.dodatkowe_informacje AS additional_information " +
              "FROM %s.zamowienia z " +
              "JOIN %s.restauracje r ON z.identyfikator_restauracji = r.identyfikator " +
              "WHERE z.identyfikator_dostawcy = USER " +
              "AND z.status = 'DOSTAWA'",
          Application.schema, Application.schema, Application.schema, Application.schema
      );

      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<DelivererOrder> orders = new ArrayList<>();
      while (resultSet.next()) {
        DelivererOrder order = new DelivererOrder();
        order.setId(resultSet.getInt("id"));
        order.setRestaurantName(resultSet.getString("restaurant_name"));
        order.setAddress(resultSet.getString("address"));
        order.setPaymentMethod(resultSet.getString("payment_method"));
        order.setPrice(resultSet.getDouble("price"));
        order.setAdditionalInformation(resultSet.getString("additional_information"));

        orders.add(order);
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
