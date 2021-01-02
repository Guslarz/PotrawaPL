package potrawa.components.frames.restaurant;

import potrawa.data.Restaurant;
import potrawa.logic.restaurant.RestaurantUserDataController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

public class RestaurantUserDataFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JButton buttonDeleteAccount;
  private JTextArea textAreaDescription;
  private JLabel labelName;

  private final JFrame parentFrame_;
  private final RestaurantUserDataController controller_;

  public RestaurantUserDataFrame(JFrame parentFrame, Connection connection) {
    super("Dane restauracji");

    parentFrame_ = parentFrame;
    controller_ = new RestaurantUserDataController(connection);

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonCancel);

    Restaurant restaurant = controller_.getUserData();
    labelName.setText(restaurant.getName());
    textAreaDescription.setText(restaurant.getDescription());

    buttonOK.addActionListener(e -> onOK());

    buttonCancel.addActionListener(e -> onCancel());

    buttonDeleteAccount.addActionListener(e -> onDeleteAccount());

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
    String description = textAreaDescription.getText();

    if (controller_.updateDescription(description)) {
      parentFrame_.setVisible(true);
      dispose();
    }
  }

  private void onDeleteAccount() {
    if (controller_.deleteAccount()) {
      JOptionPane.showMessageDialog(null,
              "Poprawnie usunięto konto - następuje zamknięcie aplikacji",
              "Sukces", JOptionPane.INFORMATION_MESSAGE);
      parentFrame_.dispose();
      dispose();
    }
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }
}
