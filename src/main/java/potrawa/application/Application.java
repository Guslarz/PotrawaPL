package potrawa.application;

import potrawa.components.frames.login.LoginFrame;

import javax.swing.*;

public class Application {

  public static final String schema = "inf141240";

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      JFrame loginDialog = new LoginFrame();
      loginDialog.setVisible(true);
    });
  }
}
