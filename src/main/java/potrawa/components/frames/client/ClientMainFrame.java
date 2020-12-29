package potrawa.components.frames.client;

import potrawa.components.frames.deliverer.DelivererUserDataFrame;
import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class ClientMainFrame extends JFrame {
  private JPanel contentPane;
  private JButton button1;
  private JButton button2;
  private JButton button3;
  private JButton button4;

  private final Connection connection_;

  public ClientMainFrame(Connection connection) {
    super("Klient");

    connection_ = connection;

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);

    button3.addActionListener(e -> onOpinionsList());

    button4.addActionListener(e -> onUserData());

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

  private void onOpinionsList() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new ClientOpinionsListFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onUserData() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new ClientUserDataFrame(this, connection_);
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
