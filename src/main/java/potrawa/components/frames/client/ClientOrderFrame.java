package potrawa.components.frames.client;

import potrawa.logic.client.ClientRestaurantController;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class ClientOrderFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JComboBox<String> comboBox1;
  private JTextField textField1;
  private JTextArea textArea1;

  private final JFrame parentFrame_;
  private final ClientRestaurantController controller_;

  public ClientOrderFrame(JFrame parentFrame, ClientRestaurantController controller) {
    super("Finalizacja zamówienia");

    parentFrame_ = parentFrame;
    controller_ = controller;

    setup();

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

  private void setup() {
    textField1.setText(controller_.getDefaultAddress());

    List<String> methods = controller_.getPaymentMethods();
    if (methods == null || methods.size() == 0) {
      comboBox1.setEnabled(false);
    } else {
      for (String method : methods) {
        comboBox1.addItem(method);
      }
      comboBox1.setSelectedIndex(0);
    }
  }

  private void onOK() {
    String paymentMethod = (String) comboBox1.getSelectedItem();
    String address = textField1.getText();
    String additionalInformation = textArea1.getText();

    if (controller_.finishOrder(paymentMethod, address, additionalInformation)) {
      controller_.newCounter();
      parentFrame_.setVisible(true);
      dispose();
    }
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }
}
