package potrawa.components.frames.deliverer;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;

public class DelivererUserDataController {

  private final Connection connection_;

  public DelivererUserDataController(Connection connection) {
    connection_ = connection;
  }

  public boolean getDelivererStatus() {
    try {
      Statement statement = connection_.createStatement();
      String query = String.format(
          "SELECT czy_dostepny " +
              "FROM %s.dostawcy " +
              "WHERE identyfikator = USER",
          Application.schema
      );
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      boolean status = resultSet.getString(1).equals("T");
      resultSet.close();
      statement.close();

      return status;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean updateDelivererStatus(boolean status) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Dostawca.Zmien_Czy_Dostepny(?);" +
              "END;",
          Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, status ? "T" : "N");
      preparedStatement.execute();
      preparedStatement.close();
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean deleteAccount() {
    try {
      Statement statement = connection_.createStatement();
      String query = String.format(
          "BEGIN " +
              "%s.Wspolne.Usun_Konto; " +
              "END;",
          Application.schema
      );
      statement.execute(query);
      statement.close();
      connection_.close();
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
