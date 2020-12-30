package potrawa.components.frames.client;

import potrawa.components.elements.RestaurantElement;
import potrawa.data.Opinion;
import potrawa.data.Restaurant;
import potrawa.logic.client.ClientRestaurantsListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class ClientRestaurantsListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonCancel;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final ClientRestaurantsListController controller_;
  private final Connection connection_;
  private final String searchString_;

  public ClientRestaurantsListFrame(JFrame parentFrame, Connection connection,
                                    String searchString) {
    super("Lista restauracji");

    parentFrame_ = parentFrame;
    controller_ = new ClientRestaurantsListController(connection);
    connection_ = connection;
    searchString_ = searchString;

    loadRestaurants(searchString);

    setContentPane(contentPane);
    setResizable(false);

    buttonCancel.addActionListener(e -> onCancel());

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  @Override
  public void setVisible(boolean b) {
    if (b) {
      loadRestaurants(searchString_);
    }

    super.setVisible(b);
  }

  private void loadRestaurants(String searchString) {
    mainPanel.removeAll();

    List<Restaurant> restaurants = searchString == null ?
        controller_.getRestaurants() : controller_.getRestaurants(searchString);

    if (restaurants == null || restaurants.size() == 0) {
      addPlaceholder();
    } else {
      addList(restaurants);
    }

    pack();
    setLocationRelativeTo(null);
  }

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak restauracji do wyświetlenia");
    mainPanel.add(label);
  }

  private void addList(List<Restaurant> restaurants) {
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    for (Restaurant restaurant : restaurants) {
      JPanel restaurantElement = new RestaurantElement(restaurant);

      JButton buttonSelect = new JButton("Wyświetl dania");
      buttonSelect.addActionListener(e -> onRestaurantSelect(restaurant));
      JPanel selectPanel = new JPanel();
      selectPanel.add(buttonSelect);
      restaurantElement.add(selectPanel);

      JButton buttonOpinion = new JButton("Wystaw opinię");
      buttonOpinion.addActionListener(e -> onOpinion(restaurant));
      JPanel opinionPanel = new JPanel();
      opinionPanel.add(buttonOpinion);
      restaurantElement.add(opinionPanel);

      listPanel.add(restaurantElement);
    }
    listPanel.add(new Box.Filler(new Dimension(0, 0),
        new Dimension(0, 500),
        new Dimension(0, 500)));

    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 500));
    mainPanel.add(scrollPane);
  }

  private void onRestaurantSelect(Restaurant restaurant) {
    JFrame nextFrame = new ClientRestaurantFrame(this, connection_, restaurant);
    nextFrame.setVisible(true);
    setVisible(false);
  }

  private void onOpinion(Restaurant restaurant) {
    Opinion opinion = controller_.getOpinion(restaurant.getId());
    JFrame nextFrame;
    if (opinion == null) {
      nextFrame = new ClientOpinionFrame(this, connection_, restaurant);
    } else {
      nextFrame = new ClientOpinionFrame(this, connection_, opinion);
    }
    nextFrame.setVisible(true);
    setVisible(false);
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  private void createUIComponents() {
    mainPanel = new JPanel();
  }
}
