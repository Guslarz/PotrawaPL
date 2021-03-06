package potrawa.logic.client;

import potrawa.application.Application;
import potrawa.logic.common.UserDataController;
import potrawa.data.Client;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;

public class ClientUserDataController extends UserDataController {

  public ClientUserDataController(Connection connection) {
    super(connection);
  }

  public Client getUserData() {
    try {
      String query = Client.query +
          "WHERE identyfikator = USER ";
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      Client client = Client.fromResultSet(resultSet);
      resultSet.close();
      statement.close();

      return client;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public boolean updateDefaultAddress(String defaultAddress) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Klient.Zmien_Domyslny_Adres(?);" +
              "END;",
          Application.schema
      );
      PreparedStatement preparedStatement = connection_.prepareStatement(query);
      preparedStatement.setString(1, defaultAddress);
      preparedStatement.execute();
      preparedStatement.close();
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
