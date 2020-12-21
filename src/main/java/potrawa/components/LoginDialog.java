package potrawa.components;

import potrawa.application.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginDialog extends JDialog {
  private JPanel contentPane;
  private JButton buttonLogin;
  private JTextField textField1;
  private JPasswordField passwordField1;
  private BiFunction<String, String, Boolean> callback_;

  public LoginDialog(BiFunction<String, String, Boolean> callback) {
    super((Dialog)null);
    callback_ = callback;
    setContentPane(contentPane);
    pack();
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonLogin);

    buttonLogin.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onLogin();
      }
    });

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onLogin() {
    String login = textField1.getText();
    String password = new String(passwordField1.getPassword());
    if (callback_.apply(login, password)) {
      dispose();
    }
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }
}
