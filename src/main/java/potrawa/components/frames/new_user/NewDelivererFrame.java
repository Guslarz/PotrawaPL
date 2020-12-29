package potrawa.components.frames.new_user;

import potrawa.logic.new_user.NewUserController;

import javax.swing.*;
import java.awt.event.*;

public class NewDelivererFrame extends JFrame {
  private final JFrame parentFrame_;
  private final NewUserController controller_;

  private JPanel contentPane;
  private JButton buttonSubmit;
  private JButton buttonCancel;
  private JTextField textField1;
  private JTextField textField2;

  public NewDelivererFrame(JFrame parentFrame, NewUserController controller) {
    super("Nowy dostawca");

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
    String surname = textField2.getText();
    if (controller_.addDeliverer(name, surname)) {
      parentFrame_.dispose();
      dispose();
    }
  }

  private void onCancel() {
    dispose();
    parentFrame_.setVisible(true);
  }
}
