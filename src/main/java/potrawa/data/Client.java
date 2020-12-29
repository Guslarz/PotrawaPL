package potrawa.data;

import potrawa.application.Application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

  private String id_;
  private String name_;
  private String surname_;
  private String defaultAddress_;

  public static final String query = String.format(
      "SELECT " +
          "identyfikator AS id, " +
          "imie AS name, " +
          "nazwisko AS surname, " +
          "domyslny_adres AS default_address " +
          "FROM %s.klienci ",
      Application.schema
  );

  public static Client fromResultSet(ResultSet resultSet) throws SQLException {
    Client client = new Client();
    client.setId(resultSet.getString("id"));
    client.setName(resultSet.getString("name"));
    client.setSurname(resultSet.getString("surname"));
    client.setDefaultAddress(resultSet.getString("default_address"));

    return client;
  }

  public String getId() {
    return id_;
  }

  public String getName() {
    return name_;
  }

  public String getSurname() {
    return surname_;
  }

  public String getDefaultAddress() {
    return defaultAddress_;
  }

  public void setId(String id) {
    id_ = id;
  }

  public void setName(String name) {
    name_ = name;
  }

  public void setSurname(String surname) {
    surname_ = surname;
  }

  public void setDefaultAddress(String defaultAddress) {
    defaultAddress_ = defaultAddress;
  }
}
