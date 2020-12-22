package potrawa.components.frames.new_user;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewUserController {

  private final Connection connection_;

  public NewUserController(Connection connection) {
    connection_ = connection;
  }

  public boolean addClient(String name, String surname, String defaultAddress) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Wspolne.Wstaw_Klienta(?, ?, ?);" +
              "%s.Autoryzacja.Klient;" +
              "END;",
          Application.schema, Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, surname);
      preparedStatement.setString(3, defaultAddress);
      preparedStatement.execute();
      preparedStatement.close();

      // TODO client initial screen
      // SwingUtilities.invokeLater();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean addDeliverer(String name, String surname) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Wspolne.Wstaw_Dostawce(?, ?);" +
              "%s.Autoryzacja.Dostawca;" +
              "END;",
          Application.schema, Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, surname);
      preparedStatement.execute();
      preparedStatement.close();

      // initial deliverer screen
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean addRestaurant(String name, String description) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Wspolne.Wstaw_Restauracje(?, ?);" +
              "%s.Autoryzacja.Restauracja;" +
              "END;",
          Application.schema, Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, description);
      preparedStatement.execute();
      preparedStatement.close();

      // initial restaurant screen
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
