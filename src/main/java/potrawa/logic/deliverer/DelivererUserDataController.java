package potrawa.logic.deliverer;

import potrawa.application.Application;
import potrawa.logic.common.UserDataController;
import potrawa.data.Deliverer;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;

public class DelivererUserDataController extends UserDataController {

  public DelivererUserDataController(Connection connection) {
    super(connection);
  }

  public Deliverer getUserData() {
    try {
      String query = Deliverer.query +
          "WHERE identyfikator = USER ";
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();

      return Deliverer.fromResultSet(resultSet);
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
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
}
