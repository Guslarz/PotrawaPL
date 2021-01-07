package potrawa.components.frames.client;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

public class ClientRestaurantSearchFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField textField1;

  private final JFrame parentFrame_;
  private final Connection connection_;

  public ClientRestaurantSearchFrame(JFrame parentFrame, Connection connection) {
    parentFrame_ = parentFrame;
    connection_ = connection;

    setContentPane(contentPane);
    getRootPane().setDefaultButton(buttonOK);
    setResizable(false);
    pack();
    setLocationRelativeTo(null);

    buttonOK.addActionListener(e -> onOK());

    buttonCancel.addActionListener(e -> onCancel());

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
    String searchString = textField1.getText();
    JFrame nextFrame = new ClientRestaurantsListFrame(parentFrame_, connection_, searchString);
    nextFrame.setVisible(true);
    dispose();
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }
}
