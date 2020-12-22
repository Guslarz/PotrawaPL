package potrawa.components.frames.new_user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class NewUserFrame extends JFrame {
  private final Connection conn_;
  private final Runnable callback_;
  private JPanel contentPane;
  private JButton buttonClient;
  private JButton buttonDeliverer;
  private JButton buttonRestaurant;

  public NewUserFrame(Connection conn, Runnable callback) {
    conn_ = conn;
    callback_ = callback;
    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);

    buttonClient.addActionListener(e -> onClient());
    buttonDeliverer.addActionListener(e -> onDeliverer());
    buttonRestaurant.addActionListener(e -> onRestaurant());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onClient() {
    setVisible(false);
    JFrame newClientFrame = new NewClientFrame(this, conn_, callback_);
    newClientFrame.setVisible(true);
  }

  private void onDeliverer() {
    setVisible(false);
    JFrame newDelivererFrame = new NewDelivererFrame(this, conn_, callback_);
    newDelivererFrame.setVisible(true);
  }

  private void onRestaurant() {
    setVisible(false);
    JFrame newRestaurantFrame = new NewRestaurantFrame(this, conn_, callback_);
    newRestaurantFrame.setVisible(true);
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }
}
