package potrawa.components.frames.restaurant;

import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;

public class RestaurantMainFrame extends JFrame {
  private JPanel contentPane;
  private JButton button1;
  private JButton button2;
  private JButton button3;
  private JButton button4;
  private JButton button5;
  private JButton button6;

  private final Connection connection_;

  public RestaurantMainFrame(Connection connection) {
    connection_ = connection;

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);

    button1.addActionListener(e -> onOrdersList());

    button2.addActionListener(e -> onOpinionsList());

    button3.addActionListener(e -> onDishesList());

    button4.addActionListener(e -> onCategory());

    button5.addActionListener(e -> onAllergen());

    button6.addActionListener(e -> onUserData());


    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> onCancel(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOrdersList() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new RestaurantOrdersListFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onOpinionsList() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new RestaurantOpinionsListFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onDishesList() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new RestaurantDishesListFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onCategory() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new RestaurantCategoryFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onAllergen() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new RestaurantAllergenFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onUserData() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new RestaurantUserDataFrame(this, connection_);
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