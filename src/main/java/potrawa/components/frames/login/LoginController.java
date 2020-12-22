package potrawa.components.frames.login;

import potrawa.application.Application;
import potrawa.components.frames.new_user.NewUserFrame;
import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

public class LoginController {

  private Connection connection_;

  private static final String connectionString_ =
      "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl";

  public boolean login(String username, String password) {
    Properties connectionProps = new Properties();
    connectionProps.put("user", username);
    connectionProps.put("password", password);

    try {
      connection_ = DriverManager.getConnection(connectionString_,
          connectionProps);
      SwingUtilities.invokeLater(this::successfulLoginCallback);
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  private void successfulLoginCallback() {
    try {
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(
          String.format("SELECT %s.Wspolne.Pobierz_Typ_Uzytkownika FROM dual", Application.schema)
      );
      resultSet.next();
      String userType = resultSet.getString(1);
      resultSet.close();
      statement.close();

      if (userType == null) {
        SwingUtilities.invokeLater(() -> {
          JFrame newUserDialog = new NewUserFrame(connection_);
          newUserDialog.setVisible(true);
        });
      } else {
        statement = connection_.createStatement();
        statement.execute("DELETE FROM UZYTKOWNICY WHERE IDENTYFIKATOR=USER");
        statement.close();
      }
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
    }
  }
}
