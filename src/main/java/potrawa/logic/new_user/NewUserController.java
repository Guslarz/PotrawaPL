package potrawa.logic.new_user;

import potrawa.application.Application;
import potrawa.components.frames.client.ClientMainFrame;
import potrawa.components.frames.deliverer.DelivererMainFrame;
import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

public class NewUserController {

  private final Connection connection_;
  private final String username_;

  private static final String adminUsername_ = "inf141240";
  private static final String adminPassword_ = "inf141240";

  public NewUserController(Connection connection, String username) {
    connection_ = connection;
    username_ = username;
  }

  public boolean addClient(String name, String surname, String defaultAddress) {
    if (!grantRole("rola_klient")) {
      return false;
    }

    try {
      String query = String.format(
          "BEGIN " +
              "%s.Wspolne.Wstaw_Klienta(?, ?, ?);" +
              "END;",
          Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, surname);
      preparedStatement.setString(3, defaultAddress);
      preparedStatement.execute();
      preparedStatement.close();

      Statement statement = connection_.createStatement();
      statement.execute("SET ROLE rola_klient");
      statement.close();

      SwingUtilities.invokeLater(() -> {
        JFrame nextFrame = new ClientMainFrame(connection_);
        nextFrame.setVisible(true);
      });
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean addDeliverer(String name, String surname) {
    if (!grantRole("rola_dostawca")) {
      return false;
    }

    try {
      String query = String.format(
          "BEGIN " +
              "%s.Wspolne.Wstaw_Dostawce(?, ?);" +
              "END;",
          Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, surname);
      preparedStatement.execute();
      preparedStatement.close();

      Statement statement = connection_.createStatement();
      statement.execute("SET ROLE rola_dostawca");
      statement.close();

      SwingUtilities.invokeLater(() -> {
        JFrame nextFrame = new DelivererMainFrame(connection_);
        nextFrame.setVisible(true);
      });
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean addRestaurant(String name, String description) {
    if (!grantRole("rola_restauracja")) {
      return false;
    }

    try {
      String query = String.format(
          "BEGIN " +
              "%s.Wspolne.Wstaw_Restauracje(?, ?);" +
              "END;",
          Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, description);
      preparedStatement.execute();
      preparedStatement.close();

      Statement statement = connection_.createStatement();
      statement.execute("SET ROLE rola_restauracja");
      statement.close();

      // initial restaurant screen
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public void close() {
    try {
      connection_.close();
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
    }
  }

  private boolean grantRole(String role) {
    Properties connectionProps = new Properties();
    connectionProps.put("user", adminUsername_);
    connectionProps.put("password", adminPassword_);

    try {
      Connection adminConnection = DriverManager.getConnection(Application.connectionString,
          connectionProps);

      String query = String.format(
          "GRANT %s TO %s",
          role, username_
      );
      Statement statement = adminConnection.createStatement();
      statement.execute(query);
      statement.close();
      adminConnection.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
