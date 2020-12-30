package potrawa.components.elements;

import potrawa.data.Dish;

import javax.swing.*;
import java.awt.*;

public class DishElement extends JPanel {

  public DishElement(Dish dish) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    JPanel namePanel = new JPanel();
    namePanel.add(new JLabel("Nazwa: "));
    namePanel.add(new JLabel(dish.getName()));
    add(namePanel);

    JPanel descPanel = new JPanel();
    descPanel.add(new JLabel("Opis: "));
    descPanel.add(new JLabel(dish.getDescription()));
    add(descPanel);

    JPanel allergensPanel = new JPanel();
    allergensPanel.add(new JLabel("Alergeny: "));
    allergensPanel.add(new JLabel(dish.getAllergens()));
    add(allergensPanel);

    JPanel pricePanel = new JPanel();
    pricePanel.add(new JLabel("Cena: "));
    pricePanel.add(new JLabel(String.format("%.2f zł", dish.getPrice())));
    add(pricePanel);
  }
}
