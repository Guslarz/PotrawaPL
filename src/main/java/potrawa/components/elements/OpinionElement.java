package potrawa.components.elements;

import potrawa.data.Opinion;

import javax.swing.*;
import java.awt.*;

public class OpinionElement extends JPanel {

  public OpinionElement(Opinion opinion) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    JPanel clientPanel = new JPanel();
    clientPanel.add(new JLabel("Klient: "));
    clientPanel.add(new JLabel(opinion.getClientName()));
    add(clientPanel);

    JPanel restaurantPanel = new JPanel();
    restaurantPanel.add(new JLabel("Restauracja: "));
    restaurantPanel.add(new JLabel(opinion.getRestaurantName()));
    add(restaurantPanel);

    JPanel ratingPanel = new JPanel();
    ratingPanel.add(new JLabel("Ocena: "));
    ratingPanel.add(new JLabel(Integer.toString(opinion.getRating())));
    add(ratingPanel);

    JPanel commentPanel = new JPanel();
    commentPanel.add(new JLabel("Komentarz: "));
    commentPanel.add(new JLabel(opinion.getComment()));
    add(commentPanel);
  }
}
