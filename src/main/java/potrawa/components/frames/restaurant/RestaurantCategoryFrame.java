package potrawa.components.frames.restaurant;

import potrawa.logic.restaurant.RestaurantCategoryController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantCategoryFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField textField1;

  private final JFrame parentFrame_;
  private final RestaurantCategoryController controller_;

  public RestaurantCategoryFrame(JFrame parentFrame, Connection connection) {
    parentFrame_ = parentFrame;
    controller_ = new RestaurantCategoryController(connection);

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOK());

    buttonCancel.addActionListener(e -> onCancel());

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(e -> onCancel(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOK() {
    String name = textField1.getText();

    try {
      Pattern regEx = Pattern.compile("[\\p{L}\\s]+");
      Matcher matcher = regEx.matcher(textField1.getText());

      if (!matcher.matches()) {
        throw new Exception();
      }

      if (controller_.insertCategory(name)) {
        parentFrame_.setVisible(true);
        dispose();
      }

    } catch (Exception ex) {
      JOptionPane.showMessageDialog(new JFrame(), "Podano nieprawidłowo nazwę kategorii!",
              "", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }
}
