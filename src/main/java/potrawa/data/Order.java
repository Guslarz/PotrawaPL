package potrawa.data;

import potrawa.application.Application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Order {

  private int id_;
  private String clientName_;
  private String delivererName_;
  private String restaurantName_;
  private String paymentMethod_;
  private String status_;
  private Timestamp timestamp_;
  private String address_;
  private String additionalInformation_;
  private double price_;
  private String dishes_;

  public static final String query = String.format(
      "SELECT " +
          "z.id_zamowienia AS id, " +
          "k.imie || ' ' || k.nazwisko AS client_name, " +
          "CASE WHEN z.identyfikator_dostawcy IS NOT NULL THEN d.imie || ' ' || d.nazwisko " +
          "     END AS deliverer_name, " +
          "r.nazwa AS restaurant_name, " +
          "z.nazwa_metody_platnosci AS payment_method, " +
          "z.status AS status, " +
          "z.data_godzina AS timestamp, " +
          "z.adres AS address, " +
          "z.dodatkowe_informacje AS additional_information, " +
          "(" +
          "    SELECT SUM(dan.cena * dwz.licznosc) " +
          "    FROM %s.dania dan " +
          "    JOIN %s.dania_w_zamowieniu dwz " +
          "    ON dan.identyfikator_restauracji = dwz.identyfikator_restauracji " +
          "       AND dan.nazwa = dwz.nazwa_dania " +
          "    WHERE dwz.id_zamowienia = z.id_zamowienia " +
          ") AS price, " +
          "(" +
          "    SELECT LISTAGG(dwz.licznosc || 'x ' || dan.nazwa, ', ') " +
          "       WITHIN GROUP (ORDER BY dan.nazwa)" +
          "    FROM %s.dania dan " +
          "    JOIN %s.dania_w_zamowieniu dwz " +
          "    ON dan.identyfikator_restauracji = dwz.identyfikator_restauracji " +
          "       AND dan.nazwa = dwz.nazwa_dania " +
          "    WHERE dwz.id_zamowienia = z.id_zamowienia " +
          ") AS dishes " +
          "FROM %s.zamowienia z " +
          "JOIN %s.restauracje r ON z.identyfikator_restauracji = r.identyfikator " +
          "JOIN %s.klienci k ON z.identyfikator_klienta = k.identyfikator " +
          "LEFT JOIN %s.dostawcy d ON z.identyfikator_dostawcy = d.identyfikator ",
      Application.schema, Application.schema, Application.schema, Application.schema,
      Application.schema, Application.schema, Application.schema, Application.schema
  );

  public static Order fromResultSet(ResultSet resultSet) throws SQLException {
    Order order = new Order();
    order.setId(resultSet.getInt("id"));
    order.setClientName(resultSet.getString("client_name"));
    order.setDelivererName(resultSet.getString("deliverer_name"));
    order.setRestaurantName(resultSet.getString("restaurant_name"));
    order.setPaymentMethod(resultSet.getString("payment_method"));
    order.setStatus(resultSet.getString("status"));
    order.setTimestamp(resultSet.getTimestamp("timestamp"));
    order.setAddress(resultSet.getString("address"));
    order.setAdditionalInformation(resultSet.getString("additional_information"));
    order.setPrice(resultSet.getDouble("price"));
    order.setDishes(resultSet.getString("dishes"));

    return order;
  }

  public int getId() {
    return id_;
  }

  public String getClientName() {
    return clientName_;
  }

  public String getDelivererName() {
    return delivererName_;
  }

  public String getRestaurantName() {
    return restaurantName_;
  }

  public String getPaymentMethod() {
    return paymentMethod_;
  }

  public String getStatus() {
    return status_;
  }

  public Timestamp getTimestamp() {
    return timestamp_;
  }

  public String getAddress() {
    return address_;
  }

  public String getAdditionalInformation() {
    return additionalInformation_;
  }

  public double getPrice() {
    return price_;
  }

  public String getDishes() {
    return dishes_;
  }

  public void setId(int id) {
    id_ = id;
  }

  public void setClientName(String name) {
    clientName_ = name;
  }

  public void setDelivererName(String name) {
    delivererName_ = name;
  }

  public void setRestaurantName(String name) {
    restaurantName_ = name;
  }

  public void setPaymentMethod(String paymentMethod) {
    paymentMethod_ = paymentMethod;
  }

  public void setStatus(String status) {
    status_ = status;
  }

  public void setTimestamp(Timestamp timestamp) {
    timestamp_ = timestamp;
  }

  public void setAddress(String address) {
    address_ = address;
  }

  public void setAdditionalInformation(String additionalInformation) {
    additionalInformation_ = additionalInformation;
  }

  public void setPrice(double price) {
    price_ = price;
  }

  public void setDishes(String dishes) {
    dishes_ = dishes;
  }
}
