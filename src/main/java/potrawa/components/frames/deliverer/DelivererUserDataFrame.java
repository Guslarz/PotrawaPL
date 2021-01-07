package potrawa.components.frames.deliverer;

import potrawa.data.Deliverer;
import potrawa.logic.deliverer.DelivererUserDataController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

public class DelivererUserDataFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JCheckBox checkBoxStatus;
  private JButton buttonDeleteAccount;
  private JLabel labelName;
  private JLabel labelSurname;

  private final JFrame parentFrame_;
  private final DelivererUserDataController controller_;

  public DelivererUserDataFrame(JFrame parentFrame, Connection connection) {
    parentFrame_ = parentFrame;
    controller_ = new DelivererUserDataController(connection);

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonCancel);

    Deliverer user = controller_.getUserData();
    labelName.setText(user.getName());
    labelSurname.setText(user.getSurname());
    checkBoxStatus.setSelected(user.getStatus());

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
    boolean status = checkBoxStatus.isSelected();

    if (controller_.updateDelivererStatus(status)) {
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
