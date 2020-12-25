package potrawa.components.elements;

import potrawa.data.Order;

import javax.swing.*;
import java.awt.*;

public class OrderElement extends JPanel {

  public OrderElement(Order order) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    JPanel titlePanel = new JPanel();
    JLabel titleLabel = new JLabel(String.format("Zamowienie #%d", order.getId()));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titlePanel.add(titleLabel);
    add(titlePanel);

    JPanel clientPanel = new JPanel();
    clientPanel.add(new JLabel("Klient: "));
    clientPanel.add(new JLabel(order.getClientName()));
    add(clientPanel);

    JPanel delivererPanel = new JPanel();
    delivererPanel.add(new JLabel("Dostawca: "));
    delivererPanel.add(new JLabel(order.getDelivererName()));
    add(delivererPanel);

    JPanel restaurantPanel = new JPanel();
    restaurantPanel.add(new JLabel("Restauracja: "));
    restaurantPanel.add(new JLabel(order.getRestaurantName()));
    add(restaurantPanel);

    JPanel paymentPanel = new JPanel();
    paymentPanel.add(new JLabel("Metoda płatności: "));
    paymentPanel.add(new JLabel(order.getPaymentMethod()));
    add(paymentPanel);

    JPanel statusPanel = new JPanel();
    statusPanel.add(new JLabel("Status zamówienia: "));
    statusPanel.add(new JLabel(order.getStatus()));
    add(statusPanel);

    JPanel timestampPanel = new JPanel();
    timestampPanel.add(new JLabel("Data: "));
    timestampPanel.add(new JLabel(order.getTimestamp().toString()));
    add(timestampPanel);

    JPanel addressPanel = new JPanel();
    addressPanel.add(new JLabel("Adres dostawy: "));
    addressPanel.add(new JLabel(order.getAddress()));
    add(addressPanel);

    JPanel additionalInformationPanel = new JPanel();
    additionalInformationPanel.add(new JLabel("Dodatkowe informacje: "));
    additionalInformationPanel.add(new JLabel(order.getAdditionalInformation()));
    add(additionalInformationPanel);

    JPanel pricePanel = new JPanel();
    pricePanel.add(new JLabel("Cena: "));
    pricePanel.add(new JLabel(String.format("%.2f zł", order.getPrice())));
    add(pricePanel);

    JPanel dishesPanel = new JPanel();
    dishesPanel.add(new JLabel("Dania: "));
    dishesPanel.add(new JLabel(order.getDishes()));
    add(dishesPanel);
  }
}
