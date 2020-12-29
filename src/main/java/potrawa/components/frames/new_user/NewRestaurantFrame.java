package potrawa.components.frames.new_user;

import potrawa.logic.new_user.NewUserController;

import javax.swing.*;
import java.awt.event.*;

public class NewRestaurantFrame extends JFrame {
  private final JFrame parentFrame_;
  private final NewUserController controller_;

  private JPanel contentPane;
  private JButton buttonSubmit;
  private JButton buttonCancel;
  private JTextField textField1;
  private JTextArea textArea;

  public NewRestaurantFrame(JFrame parentFrame, NewUserController controller) {
    super("Nowa restauracja");

    parentFrame_ = parentFrame;
    controller_ = controller;

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonSubmit);

    buttonSubmit.addActionListener(e -> onSubmit());

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

  private void onSubmit() {
    String name = textField1.getText();
    String description = textArea.getText();
    if (controller_.addRestaurant(name, description)) {
      parentFrame_.dispose();
      dispose();
    }
  }

  private void onCancel() {
    dispose();
    parentFrame_.setVisible(true);
  }
}
