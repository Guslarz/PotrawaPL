package potrawa.components.frames.restaurant;

import potrawa.components.elements.OrderElement;
import potrawa.data.Order;
import potrawa.logic.restaurant.RestaurantOrdersListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class RestaurantOrdersListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonCancel;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final RestaurantOrdersListController controller_;
  private final Connection connection_;

  public RestaurantOrdersListFrame(JFrame parentFrame, Connection connection) {
    parentFrame_ = parentFrame;
    controller_ = new RestaurantOrdersListController(connection);
    connection_ = connection;

    loadOrders();

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);

    buttonCancel.addActionListener(e -> onCancel());

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(e -> onCancel(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void loadOrders() {
    mainPanel.removeAll();
    List<Order> orders = controller_.getOrders();

    if (orders == null || orders.size() == 0) {
      addPlaceholder();
    } else {
      addList(orders);
    }
  }

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak zamówień do wyświetlenia");
    mainPanel.add(label);
  }

  private void addList(List<Order> orders) {
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

    for (Order order : orders) {
      JPanel updatePanel = new JPanel();
      JPanel orderElement = new OrderElement(order);
      if (order.getStatus().equals("REALIZACJA")) {
        JButton buttonUpdate = new JButton("Modyfikuj status");
        buttonUpdate.addActionListener(e -> SwingUtilities.invokeLater(() -> {
          RestaurantOrderFrame nextFrame =
                  new RestaurantOrderFrame(this, connection_, order);
          setVisible(false);
          nextFrame.setVisible(true);
        }));

        updatePanel.add(buttonUpdate);
      }

      orderElement.add(updatePanel);
      listPanel.add(orderElement);
    }

    listPanel.add(new Box.Filler(new Dimension(0, 0),
            new Dimension(0, 500),
            new Dimension(0, 500)));

    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 500));
    mainPanel.add(scrollPane);
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  private void createUIComponents() {
    mainPanel = new JPanel();
  }

  @Override
  public void setVisible(boolean b) {
    if (b) {
      loadOrders();
    }

    super.setVisible(b);
  }
}
