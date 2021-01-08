package potrawa.components.frames.login;

import potrawa.logic.login.LoginController;

import javax.swing.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonLogin;
  private JTextField textField1;
  private JPasswordField passwordField1;

  private final LoginController controller_;

  public LoginFrame() {
    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonLogin);

    controller_ = new LoginController();

    buttonLogin.addActionListener(e -> onLogin());

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

  private void onLogin() {
    String username = textField1.getText();
    String password = new String(passwordField1.getPassword());
    if (controller_.login(username, password)) {
      dispose();
    }
  }

  private void onCancel() {
    dispose();
  }
}
