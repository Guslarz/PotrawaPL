package potrawa.application;

import potrawa.components.frames.login.LoginFrame;
import potrawa.components.frames.new_user.NewUserFrame;
import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

public class Application {

  private Connection conn_;

  private static final String connectionString_ =
      "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl";
  public static final String schema = "inf141240";

  public Application() {
    JFrame loginDialog = new LoginFrame(this::loginCallback);
    loginDialog.setVisible(true);
  }

  public static void main(String args[]) {
    SwingUtilities.invokeLater(() -> {
      Application app = new Application();
    });
  }

  private Boolean loginCallback(String login, String password) {
    Properties connectionProps = new Properties();
    connectionProps.put("user", login);
    connectionProps.put("password", password);

    try {
      conn_ = DriverManager.getConnection(connectionString_,
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
      Statement statement = conn_.createStatement();
      ResultSet resultSet = statement.executeQuery(
          String.format("SELECT %s.Wspolne.Pobierz_Typ_Uzytkownika FROM dual", schema)
      );
      resultSet.next();
      String userType = resultSet.getString(1);
      resultSet.close();
      statement.close();

      if (userType == null) {
        JFrame newUserDialog = new NewUserFrame(conn_, this::successfulLoginCallback);
        newUserDialog.setVisible(true);
      } else {
        System.out.println(userType);
      }

    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
    }
  }
}
