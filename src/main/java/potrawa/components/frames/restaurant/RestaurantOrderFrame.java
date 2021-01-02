package potrawa.components.frames.restaurant;

import potrawa.data.Deliverer;
import potrawa.data.Order;
import potrawa.logic.restaurant.RestaurantOrderController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class RestaurantOrderFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JComboBox comboBox1;

  private final JFrame parentFrame_;
  private final RestaurantOrderController controller_;
  private final Order order_;

  public RestaurantOrderFrame(JFrame parentFrame, Connection connection, Order order) {
    super("Modyfikacja zamówienia");

    parentFrame_ = parentFrame;
    controller_ = new RestaurantOrderController(connection);
    order_ = order;

    List<Deliverer> deliverers = controller_.getDeliverers();
    if (deliverers == null || deliverers.size() == 0) {
      comboBox1.setEnabled(false);
    } else {
      for (Deliverer deliverer : deliverers) {
        comboBox1.addItem(deliverer);
      }
      comboBox1.setSelectedIndex(0);
    }

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);

    buttonOK.addActionListener(e -> onOK());

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

  private void onOK() {
    Deliverer selectedDeliverer = (Deliverer) comboBox1.getSelectedItem();

    if (controller_.updateStatus(order_.getId(), selectedDeliverer.getId())) {
      parentFrame_.setVisible(true);
      dispose();
    }
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }
}
