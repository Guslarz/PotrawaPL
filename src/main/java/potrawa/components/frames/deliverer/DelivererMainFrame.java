package potrawa.components.frames.deliverer;

import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class DelivererMainFrame extends JFrame {
  private JPanel contentPane;
  private JButton button1;
  private JButton button2;

  private Connection connection_;

  public DelivererMainFrame(Connection connection) {
    super("Dostawca");

    connection_ = connection;

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(button1);

    button1.addActionListener(e -> onOrdersList());

    button2.addActionListener(e -> onUserData());

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

  private void onOrdersList() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new DelivererOrdersListFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onUserData() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new DelivererUserDataFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onCancel() {
    try {
      connection_.close();
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
    }
    dispose();
  }
}
