package potrawa.logic.common;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDataController {

  protected Connection connection_;

  public UserDataController(Connection connection) {
    connection_ = connection;
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
