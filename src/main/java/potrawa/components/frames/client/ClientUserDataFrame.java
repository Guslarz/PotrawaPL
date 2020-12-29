package potrawa.components.frames.client;

import potrawa.data.Client;
import potrawa.logic.client.ClientUserDataController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

public class ClientUserDataFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JButton buttonDeleteAccount;
  private JTextField textField1;
  private JLabel labelName;
  private JLabel labelSurname;

  private final JFrame parentFrame_;
  private final ClientUserDataController controller_;

  public ClientUserDataFrame(JFrame parentFrame, Connection connection) {
    super("Dane klienta");

    parentFrame_ = parentFrame;
    controller_ = new ClientUserDataController(connection);

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonCancel);

    Client client = controller_.getUserData();
    labelName.setText(client.getName());
    labelSurname.setText(client.getSurname());
    textField1.setText(client.getDefaultAddress());

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
    String defaultAddress = textField1.getText();

    if (controller_.updateDefaultAddress(defaultAddress)) {
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
