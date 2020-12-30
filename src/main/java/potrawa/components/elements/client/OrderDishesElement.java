package potrawa.components.elements.client;

import potrawa.data.Dish;
import potrawa.logic.client.ClientRestaurantController;
import potrawa.logic.client.OrderListener;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class OrderDishesElement extends JPanel implements OrderListener {

  private final ClientRestaurantController controller_;

  public OrderDishesElement(ClientRestaurantController controller) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    controller_ = controller;
    controller_.addListener(this);
    updateOrder(controller_.getDishesCounter());
  }

  @Override
  public void updateOrder(Map<Dish, Integer> dishesCounter) {
    removeAll();

    if (dishesCounter.size() == 0) {
      addPlaceholder();
    } else {
      addList(dishesCounter);
    }

    revalidate();
    repaint();
  }

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak dań w zamówieniu");
    label.setPreferredSize(new Dimension(500, 500));
    add(label);
  }

  private void addList(Map<Dish, Integer> dishesCounter) {
    double sum = 0;

    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

    for (Map.Entry<Dish, Integer> entry : dishesCounter.entrySet()) {
      Dish dish = entry.getKey();
      int count = entry.getValue();

      sum += dish.getPrice() * count;

      JPanel entryPanel = new JPanel();
      entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));

      JPanel titlePanel = new JPanel();
      String title = String.format("%dx %s", count, dish.getName());
      titlePanel.add(new JLabel(title));
      entryPanel.add(titlePanel);

      JPanel pricePanel = new JPanel();
      pricePanel.add(new JLabel("Cena: "));
      String price = String.format("%.2f zł", dish.getPrice() * count);
      pricePanel.add(new JLabel(price));

      JPanel buttonPanel = new JPanel();
      JButton buttonAdd = new JButton("+");
      buttonAdd.addActionListener(e -> controller_.addDish(dish));
      buttonPanel.add(buttonAdd);
      JButton buttonRemove = new JButton("-");
      buttonRemove.addActionListener(e -> controller_.removeDish(dish));
      buttonPanel.add(buttonRemove);
      entryPanel.add(buttonPanel);

      listPanel.add(entryPanel);
    }
    listPanel.add(new Box.Filler(new Dimension(0, 0),
        new Dimension(0, Short.MAX_VALUE),
        new Dimension(0, Short.MAX_VALUE)));

    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 400));
    add(scrollPane);

    JPanel sumPanel = new JPanel();
    sumPanel.add(new JLabel("Suma: "));
    sumPanel.add(new JLabel(String.format("%.2f zł", sum)));
    add(sumPanel);
  }
}
