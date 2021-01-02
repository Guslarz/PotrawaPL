package potrawa.logic.login;

import potrawa.application.Application;
import potrawa.components.frames.client.ClientMainFrame;
import potrawa.components.frames.deliverer.DelivererMainFrame;
import potrawa.components.frames.new_user.NewUserFrame;
import potrawa.components.frames.restaurant.RestaurantMainFrame;
import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.sql.*;
import java.util.Properties;

public class LoginController {

  private Connection connection_;
  private String username_;

  public boolean login(String username, String password) {
    Properties connectionProps = new Properties();
    connectionProps.put("user", username);
    connectionProps.put("password", password);
    username_ = username;

    try {
      connection_ = DriverManager.getConnection(Application.connectionString,
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
          JFrame newUserDialog = new NewUserFrame(connection_, username_);
          newUserDialog.setVisible(true);
        });
      } else {
        switch (userType) {
          case "KLIENT":
            SwingUtilities.invokeLater(() -> {
              JFrame nextFrame = new ClientMainFrame(connection_);
              nextFrame.setVisible(true);
            });
            break;
          case "DOSTAWCA":
            SwingUtilities.invokeLater(() -> {
              JFrame nextFrame = new DelivererMainFrame(connection_);
              nextFrame.setVisible(true);
            });
            break;
          case "RESTAURACJA":
            SwingUtilities.invokeLater(() -> {
              JFrame nextFrame = new RestaurantMainFrame(connection_);
              nextFrame.setVisible(true);
            });
            break;
          default:  // Remove later
            statement = connection_.createStatement();
            statement.execute(
                "BEGIN " +
                    "inf141240.Wspolne.USUN_KONTO(); " +
                    "END; "
            );
            statement.close();
            break;
        }
      }
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
    }
  }
}
