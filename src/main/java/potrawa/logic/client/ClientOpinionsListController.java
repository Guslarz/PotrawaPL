package potrawa.logic.client;

import potrawa.application.Application;
import potrawa.data.Opinion;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientOpinionsListController {

  private final Connection connection_;

  public ClientOpinionsListController(Connection connection) {
    connection_ = connection;
  }

  public List<Opinion> getOpinions() {
    try {
      String query = Opinion.query +
          "WHERE identyfikator_klienta = USER";
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<Opinion> opinions = new ArrayList<>();
      while (resultSet.next()) {
        opinions.add(Opinion.fromResultSet(resultSet));
      }

      return opinions;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public boolean deleteOpinion(String restaurantId) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Klient.Usun_Opinie(?); " +
              "END; ",
          Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, restaurantId);
      statement.execute();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
