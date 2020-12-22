package potrawa.components.frames.new_user;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

public class NewUserFrame extends JFrame {
  private final NewUserController controller_;

  private JPanel contentPane;
  private JButton buttonClient;
  private JButton buttonDeliverer;
  private JButton buttonRestaurant;

  public NewUserFrame(Connection connection) {
    controller_ = new NewUserController(connection);

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);

    buttonClient.addActionListener(e -> onClient());
    buttonDeliverer.addActionListener(e -> onDeliverer());
    buttonRestaurant.addActionListener(e -> onRestaurant());

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

  private void onClient() {
    setVisible(false);
    JFrame newClientFrame = new NewClientFrame(this, controller_);
    newClientFrame.setVisible(true);
  }

  private void onDeliverer() {
    setVisible(false);
    JFrame newDelivererFrame = new NewDelivererFrame(this, controller_);
    newDelivererFrame.setVisible(true);
  }

  private void onRestaurant() {
    setVisible(false);
    JFrame newRestaurantFrame = new NewRestaurantFrame(this, controller_);
    newRestaurantFrame.setVisible(true);
  }

  private void onCancel() {
    dispose();
  }
}
