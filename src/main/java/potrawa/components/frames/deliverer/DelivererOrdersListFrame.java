package potrawa.components.frames.deliverer;

import potrawa.components.elements.OrderElement;
import potrawa.data.Order;
import potrawa.logic.deliverer.DelivererOrdersListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class DelivererOrdersListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonCancel;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final DelivererOrdersListController controller_;

  public DelivererOrdersListFrame(JFrame parentFrame, Connection connection) {
    super("Zamówienia dostawcy");

    parentFrame_ = parentFrame;
    controller_ = new DelivererOrdersListController(connection);

    List<Order> orders = controller_.getOrders();

    if (orders == null || orders.size() == 0) {
      addPlaceholder();
    } else {
      addList(orders);
    }

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
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak zamówień do wyświetlenia");
    mainPanel.add(label);
  }

  private void addList(List<Order> orders) {
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    for (Order order : orders) {
      JPanel orderElement = new OrderElement(order);
      JButton button = new JButton("Oznacz jako dostarczone");
      button.addActionListener(e -> {
        controller_.confirmDelivery(order.getId());
        button.setEnabled(false);
      });
      orderElement.add(new JPanel() {{
        add(button);
      }});
      listPanel.add(orderElement);
    }
    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 600));
    mainPanel.add(scrollPane);
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  private void createUIComponents() {
    mainPanel = new JPanel();
  }
}
