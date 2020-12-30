package potrawa.components.frames.client;

import potrawa.components.elements.OrderElement;
import potrawa.data.Order;
import potrawa.logic.client.ClientOrdersListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class ClientOrdersListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final ClientOrdersListController controller_;

  public ClientOrdersListFrame(JFrame parentFrame, Connection connection) {
    super("Zamówienia");

    parentFrame_ = parentFrame;
    controller_ = new ClientOrdersListController(connection);

    loadOrders();

    setContentPane(contentPane);
    getRootPane().setDefaultButton(buttonOK);
    setResizable(false);
    pack();
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

  private void loadOrders() {
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
      JPanel orderElement = new OrderElement(order);
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
}
