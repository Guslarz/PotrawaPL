package potrawa.logic.client;

import potrawa.application.Application;
import potrawa.data.Dish;
import potrawa.error.DefaultSqlHandler;

import java.sql.*;
import java.util.*;

public class ClientRestaurantController {

  private final Connection connection_;
  private final String restaurantId_;
  private Map<Dish, Integer> dishesCounter_;
  private final Collection<OrderListener> listeners_;

  public ClientRestaurantController(Connection connection, String restaurantId) {
    connection_ = connection;
    restaurantId_ = restaurantId;
    dishesCounter_ = new HashMap<>();
    listeners_ = new ArrayList<>();
  }

  public Map<Dish, Integer> getDishesCounter() {
    return dishesCounter_;
  }

  public void addListener(OrderListener listener) {
    listeners_.add(listener);
  }

  public void addDish(Dish dish) {
    if (dishesCounter_.containsKey(dish)) {
      dishesCounter_.put(dish, dishesCounter_.get(dish) + 1);
    } else {
      dishesCounter_.put(dish, 1);
    }
    notifyListeners();
  }

  public void removeDish(Dish dish) {
    int value = dishesCounter_.get(dish);
    if (value == 1) {
      dishesCounter_.remove(dish);
    } else {
      dishesCounter_.put(dish, value - 1);
    }
    notifyListeners();
  }

  public void newCounter() {
    dishesCounter_ = new HashMap<>();
    notifyListeners();
  }

  public List<Dish> getDishes() {
    try {
      String query = Dish.query +
          "WHERE identyfikator_restauracji = ? " +
          "ORDER BY category ";
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setString(1, restaurantId_);
      ResultSet resultSet = statement.executeQuery();

      List<Dish> dishes = new ArrayList<>();
      while (resultSet.next()) {
        dishes.add(Dish.fromResultSet(resultSet));
      }
      resultSet.close();
      statement.close();

      return dishes;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public String getDefaultAddress() {
    try {
      String query = String.format(
          "SELECT domyslny_adres " +
              "FROM %s.klienci " +
              "WHERE identyfikator = USER ",
          Application.schema
      );
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);
      resultSet.next();
      String address = resultSet.getString(1);
      resultSet.close();
      statement.close();

      return address;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public List<String> getPaymentMethods() {
    try {
      String query = String.format(
          "SELECT nazwa " +
              "FROM %s.metody_platnosci ",
          Application.schema
      );
      Statement statement = connection_.createStatement();
      ResultSet resultSet = statement.executeQuery(query);

      List<String> methods = new ArrayList<>();
      while (resultSet.next()) {
        methods.add(resultSet.getString(1));
      }
      resultSet.close();
      statement.close();

      return methods;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return null;
    }
  }

  public boolean finishOrder(String paymentMethod, String address,
                             String additionalInformation) {
    try {
      connection_.setAutoCommit(false);
      int orderId = addOrder(paymentMethod, address, additionalInformation);
      addDishesToOrder(orderId);
      connection_.commit();
      connection_.setAutoCommit(true);
      return true;
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
      return false;
    }
  }

  private int addOrder(String paymentMethod, String address,
                       String additionalInformation) throws SQLException {
    String query = String.format(
        "{ ? = call %s.Klient.Wstaw_Zamowienie(?, ?, ?, ?) }",
        Application.schema
    );
    CallableStatement statement = connection_.prepareCall(query);
    statement.registerOutParameter(1, JDBCType.NUMERIC);
    statement.setString(2, restaurantId_);
    statement.setString(3, paymentMethod);
    statement.setString(4, address);
    statement.setString(5, additionalInformation);

    statement.executeUpdate();
    int orderId = statement.getInt(1);

    statement.close();
    return orderId;
  }

  private void addDishesToOrder(int orderId) throws SQLException {
    for (Map.Entry<Dish, Integer> entry : dishesCounter_.entrySet()) {
      Dish dish = entry.getKey();
      int count = entry.getValue();

      String query = String.format(
          "BEGIN " +
              "%s.Klient.Wstaw_Dania_W_Zamowieniu(?, ?, ?, ?); " +
              "END; ",
          Application.schema
      );
      PreparedStatement statement = connection_.prepareStatement(query);
      statement.setInt(1, orderId);
      statement.setString(2, dish.getRestaurantId());
      statement.setString(3, dish.getName());
      statement.setInt(4, count);

      statement.execute();
      statement.close();
    }
  }

  private void notifyListeners() {
    for (OrderListener listener : listeners_) {
      listener.updateOrder(dishesCounter_);
    }
  }
}
