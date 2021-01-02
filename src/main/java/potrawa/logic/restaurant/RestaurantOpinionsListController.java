package potrawa.logic.restaurant;

import potrawa.data.Opinion;
import potrawa.error.DefaultSqlHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RestaurantOpinionsListController {

  private final Connection connection_;

  public RestaurantOpinionsListController(Connection connection) {
    connection_ = connection;
  }

  public List<Opinion> getOpinions() {
    try {
      String query = Opinion.query +
              "WHERE identyfikator_restauracji = USER ";
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<Opinion> opinions = new ArrayList<>();
      while (resultSet.next()) {
        opinions.add(Opinion.fromResultSet(resultSet));
      }
      resultSet.close();
      statement.close();

      return opinions;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

}
