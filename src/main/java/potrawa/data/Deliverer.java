package potrawa.data;

import potrawa.application.Application;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Deliverer {

  private String id_;
  private String name_;
  private String surname_;
  private boolean status_;

  public static final String query = String.format(
      "SELECT " +
          "identyfikator AS id, " +
          "imie AS name, " +
          "nazwisko AS surname, " +
          "CASE czy_dostepny WHEN 'T' THEN 1 ELSE 0 END AS status " +
          "FROM %s.dostawcy ",
      Application.schema
  );

  public static Deliverer fromResultSet(ResultSet resultSet) throws SQLException {
    Deliverer deliverer = new Deliverer();
    deliverer.setId(resultSet.getString("id"));
    deliverer.setName(resultSet.getString("name"));
    deliverer.setSurname(resultSet.getString("surname"));
    deliverer.setStatus(resultSet.getBoolean("status"));

    return deliverer;
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

  public boolean getStatus() {
    return status_;
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

  public void setStatus(boolean status) {
    status_ = status;
  }


  @Override
  public String toString() {
    return name_ + " " + surname_;
  }
}
