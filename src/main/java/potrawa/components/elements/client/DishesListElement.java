package potrawa.components.elements.client;

import potrawa.components.elements.DishElement;
import potrawa.data.Dish;
import potrawa.logic.client.ClientRestaurantController;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DishesListElement extends JPanel {

  private final ClientRestaurantController controller_;

  public DishesListElement(ClientRestaurantController controller) {
    controller_ = controller;

    loadDishes();
  }

  private void loadDishes() {
    List<Dish> dishes = controller_.getDishes();

    if (dishes == null || dishes.size() == 0) {
      addPlaceholder();
    } else {
      addList(dishes);
    }
  }

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak dań do wyświetlenia");
    label.setPreferredSize(new Dimension(500, 500));
    add(label);
  }

  private void addList(List<Dish> dishes) {
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

    String category = dishes.get(0).getCategory();
    Consumer<String> addCategory = str -> {
      JPanel categoryPanel = new JPanel();
      String label = str == null ? "Pozostałe" : str;
      categoryPanel.add(new JLabel(label));
      listPanel.add(categoryPanel);
    };
    addCategory.accept(category);

    for (Dish dish : dishes) {
      if ((dish.getCategory() != null && category == null) ||
          (category != null && !category.equals(dish.getCategory()))) {
        category = dish.getCategory();
        addCategory.accept(category);
      }

      DishElement dishElement = new DishElement(dish);

      JPanel addPanel = new JPanel();
      JButton buttonAdd = new JButton("Dodaj do zamówienia");
      buttonAdd.addActionListener(e -> controller_.addDish(dish));
      addPanel.add(buttonAdd);
      dishElement.add(addPanel);

      listPanel.add(dishElement);
    }
    listPanel.add(new Box.Filler(new Dimension(0, 0),
        new Dimension(0, Short.MAX_VALUE),
        new Dimension(0, Short.MAX_VALUE)));

    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 500));
    add(scrollPane);
  }
}
