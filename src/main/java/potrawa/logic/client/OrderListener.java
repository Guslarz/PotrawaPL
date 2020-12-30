package potrawa.logic.client;

import potrawa.data.Dish;

import java.util.Map;

public interface OrderListener {
  void updateOrder(Map<Dish, Integer> dishesCounter);
}
