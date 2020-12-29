package potrawa.components.frames.client;

import potrawa.data.Opinion;
import potrawa.logic.client.ClientOpinionController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;

public class ClientOpinionFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JComboBox<Integer> comboBox1;
  private JTextArea textArea1;
  private JLabel labelRestaurantName;

  private final JFrame parentFrame_;
  private final ClientOpinionController controller_;
  private String restaurantId_;

  private ClientOpinionFrame(JFrame parentFrame, Connection connection) {
    super("Opinia o restauracji");

    parentFrame_ = parentFrame;
    controller_ = new ClientOpinionController(connection);

    setContentPane(contentPane);
    setResizable(false);
    getRootPane().setDefaultButton(buttonOK);

    comboBox1.addItem(1);
    comboBox1.addItem(2);
    comboBox1.addItem(3);
    comboBox1.addItem(4);
    comboBox1.addItem(5);
    comboBox1.setSelectedIndex(4);

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

  public ClientOpinionFrame(JFrame parentFrame, Connection connection, Opinion opinion) {
    this(parentFrame, connection);
    restaurantId_ = opinion.getRestaurantId();

    labelRestaurantName.setText(opinion.getRestaurantName());
    comboBox1.setSelectedItem(opinion.getRating());
    textArea1.setText(opinion.getComment());

    pack();
    setLocationRelativeTo(null);

    buttonOK.addActionListener(e -> onOKUpdate());
  }

  public ClientOpinionFrame(JFrame parentFrame, Connection connection, String restaurantId,
                            String restaurantName) {
    this(parentFrame, connection);
    restaurantId_ = restaurantId;

    labelRestaurantName.setText(restaurantName);

    pack();
    setLocationRelativeTo(null);

    buttonOK.addActionListener(e -> onOKInsert());
  }

  private void onOKUpdate() {
    int rating = (int) comboBox1.getSelectedItem();
    String comment = textArea1.getText();

    if (controller_.updateOpinion(restaurantId_, rating, comment)) {
      parentFrame_.setVisible(true);
      dispose();
    }
  }

  private void onOKInsert() {
    int rating = (int) comboBox1.getSelectedItem();
    String comment = textArea1.getText();

    if (controller_.insertOpinion(restaurantId_, rating, comment)) {
      parentFrame_.setVisible(true);
      dispose();
    }
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }
}
