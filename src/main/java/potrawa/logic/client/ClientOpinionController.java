package potrawa.logic.client;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientOpinionController {

  private final Connection connection_;

  public ClientOpinionController(Connection connection) {
    connection_ = connection;
  }

  public boolean updateOpinion(String restaurantId, int rating, String comment) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Klient.Zmien_Opinie(?, ?, ?); " +
              "END; ",
          Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, restaurantId);
      statement.setInt(2, rating);
      statement.setString(3, comment);
      statement.execute();
      statement.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  public boolean insertOpinion(String restaurantId, int rating, String comment) {
    try {
      String query = String.format(
          "BEGIN " +
              "%s.Klient.Wstaw_Opinie(?, ?, ?); " +
              "END; ",
          Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, restaurantId);
      statement.setInt(2, rating);
      statement.setString(3, comment);
      statement.execute();
      statement.close();

      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }
}
