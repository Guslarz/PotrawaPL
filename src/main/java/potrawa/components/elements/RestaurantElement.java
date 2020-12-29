package potrawa.components.elements;

import potrawa.data.Restaurant;

import javax.swing.*;
import java.awt.*;

public class RestaurantElement extends JPanel {

  public RestaurantElement(Restaurant restaurant) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    JPanel namePanel = new JPanel();
    namePanel.add(new JLabel("Nazwa: "));
    namePanel.add(new JLabel(restaurant.getName()));
    add(namePanel);

    JPanel ratingPanel = new JPanel();
    ratingPanel.add(new JLabel("Średnia ocena: "));
    String rating = restaurant.getAvgRating() == 0 ?
        "---" : String.format("%.2f", restaurant.getAvgRating());
    ratingPanel.add(new JLabel(rating));
    add(ratingPanel);

    JPanel descPanel = new JPanel();
    descPanel.add(new JLabel("Opis: "));
    descPanel.add(new JLabel(restaurant.getDescription()));
    add(descPanel);
  }
}
