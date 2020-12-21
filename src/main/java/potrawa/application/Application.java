package potrawa.application;

import potrawa.components.standalone.LoginDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Application {

  private Connection conn_;
  private JDialog loginDialog_;

  private static final String connectionString_ =
      "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/dblab02_students.cs.put.poznan.pl";
  private static final String schema_ = "inf141240";

  public Application() {
    loginDialog_ = new LoginDialog(this::loginCallback);
    loginDialog_.setVisible(true);
  }

  public static void main(String args[]) {
    EventQueue.invokeLater(() -> {
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
      EventQueue.invokeLater(this::afterLogin);
      return true;
    } catch (SQLException ex) {
      String msg;
      switch (ex.getErrorCode()) {
        case 1017:
          msg = "Błędny login lub hasło";
          break;
        case 17002:
          msg = "Brak połączenia";
          break;
        default:
          msg = "Nieznany błąd";
          break;
      }
      JOptionPane.showMessageDialog(loginDialog_,
          msg, "Błąd", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  private void afterLogin() {
    System.out.println("XD");
  }
}
