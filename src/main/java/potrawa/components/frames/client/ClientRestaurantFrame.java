package potrawa.components.frames.client;

import potrawa.components.elements.client.DishesListElement;
import potrawa.components.elements.client.OrderDishesElement;
import potrawa.data.Restaurant;
import potrawa.logic.client.ClientRestaurantController;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class ClientRestaurantFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JLabel labelRestaurantName;
  private JPanel dishesPanel;
  private JPanel orderPanel;
  private JComboBox comboBox1;
  private JButton buttonCategory;
  private JButton buttonAllCategories;

  private final JFrame parentFrame_;
  private final ClientRestaurantController controller_;

  public ClientRestaurantFrame(JFrame parentFrame, Connection connection,
                               Restaurant restaurant) {
    parentFrame_ = parentFrame;
    controller_ = new ClientRestaurantController(connection, restaurant.getId());

    setupCategories();

    labelRestaurantName.setText(restaurant.getName());
    dishesPanel.add(new DishesListElement(controller_));
    orderPanel.add(new OrderDishesElement(controller_));

    setContentPane(contentPane);
    getRootPane().setDefaultButton(buttonOK);
    setResizable(false);
    pack();
    setLocationRelativeTo(null);

    buttonCategory.addActionListener(e -> onCategory());

    buttonAllCategories.addActionListener(e -> onAllCategories());

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

  private void setupCategories() {
    List<String> categories = controller_.getCategories();
    if (categories == null || categories.size() == 0) {
      comboBox1.setEnabled(false);
    } else {
      for (String category : categories) {
        comboBox1.addItem(category);
      }
      comboBox1.setSelectedIndex(0);
    }
  }

  private void onCategory() {
    dishesPanel.removeAll();
    dishesPanel.add(new DishesListElement(controller_, (String)comboBox1.getSelectedItem()));

    pack();
    setLocationRelativeTo(null);
  }

  private void onAllCategories() {
    dishesPanel.removeAll();
    dishesPanel.add(new DishesListElement(controller_));

    pack();
    setLocationRelativeTo(null);
  }

  private void onOK() {
    if (controller_.getDishesCounter().size() == 0) {
      JOptionPane.showMessageDialog(null, "Brak dań w zamówieniu",
          "Błąd", JOptionPane.ERROR_MESSAGE);
    } else {
      JFrame nextFrame = new ClientOrderFrame(this, controller_);
      setVisible(false);
      nextFrame.setVisible(true);
    }
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  private void createUIComponents() {
    dishesPanel = new JPanel();
    orderPanel = new JPanel();
  }
}
