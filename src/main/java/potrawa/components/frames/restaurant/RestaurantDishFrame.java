package potrawa.components.frames.restaurant;

import potrawa.data.Dish;
import potrawa.logic.restaurant.RestaurantDishController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantDishFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextArea textAreaDescription;
  private JTextField textFieldPrice;
  private JTextField textFieldName;
  private JComboBox comboBox1;
  private JPanel allergensPanel;

  private final JFrame parentFrame_;
  private final RestaurantDishController controller_;

  private List<JCheckBox> checkboxes_ = new ArrayList<>();

  public RestaurantDishFrame(JFrame parentFrame, Connection connection) {
    parentFrame_ = parentFrame;
    controller_ = new RestaurantDishController(connection);

    setupCategories();
    setupAllergens();

    setContentPane(contentPane);
    setResizable(false);
    pack();
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> onOKInsert());

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

  public RestaurantDishFrame(JFrame parentFrame, Connection connection, Dish dish) {
    parentFrame_ = parentFrame;
    controller_ = new RestaurantDishController(connection);

    setupCategories();
    setupAllergens(dish);

    setContentPane(contentPane);
    setResizable(false);
    pack();
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonOK);

    textFieldName.setEditable(false);
    textFieldName.setText(dish.getName());
    textAreaDescription.setText(dish.getDescription());
    textFieldPrice.setText(Double.toString(dish.getPrice()));
    comboBox1.setSelectedIndex(comboBox1.getSelectedIndex());

    buttonOK.addActionListener(e -> onOKUpdate());

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

  private void setupAllergens() {
    List<String> allergens = controller_.getAllergens();

    if (allergens == null || allergens.size() == 0) {
      JLabel label = new JLabel();
      label.setText("brak alergenów w bazie");
      allergensPanel.add(label);
    } else {
      for (String allergen : allergens) {
        JCheckBox checkBox = new JCheckBox(allergen);
        checkBox.setName(allergen);
        checkboxes_.add(checkBox);
        allergensPanel.add(checkBox);
      }
    }
  }

  private void setupAllergens(Dish dish) {
    List<String> allergens = controller_.getAllergens();
    List<String> dishAllergens = controller_.getDishAllergens(dish.getName());

    if (allergens == null || allergens.size() == 0) {
      JLabel label = new JLabel();
      label.setText("brak alergenów w bazie");
      allergensPanel.add(label);
    } else {
      for (String allergen : allergens) {
        JCheckBox checkBox = new JCheckBox(allergen);
        checkBox.setName(allergen);
        if (dishAllergens != null) {
          checkBox.setSelected(dishAllergens.contains(allergen));
        }
        checkboxes_.add(checkBox);
        allergensPanel.add(checkBox);
      }
    }
  }

  private void onOKUpdate() {
    String name = textFieldName.getText();
    String description = textAreaDescription.getText();
    String category = (String) comboBox1.getSelectedItem();

    List<String> prevAllergens = controller_.getDishAllergens(name);
    List<String> insertedAllergens = new ArrayList<>();
    List<String> deletedAllergens = new ArrayList<>();

    for (JCheckBox box : checkboxes_) {
      if (box.isSelected() && !prevAllergens.contains(box.getText())) {
        insertedAllergens.add(box.getText());
      } else if (!box.isSelected() && prevAllergens.contains(box.getText())) {
        deletedAllergens.add(box.getText());
      }
    }

    try {
      double price = Double.valueOf(textFieldPrice.getText());
      Pattern regEx = Pattern.compile("\\d{1,3}(?:.\\d{0,2})?");
      Matcher matcher = regEx.matcher(textFieldPrice.getText());

      if (!matcher.matches()) {
        throw new NumberFormatException();
      }

      if (controller_.finishUpdatingDish(name, description, price,
              category, insertedAllergens, deletedAllergens)) {
        parentFrame_.setVisible(true);
        dispose();
      }

    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(new JFrame(), "Podano nieprawidłowo cenę!",
              "Cena", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onOKInsert() {
    String name = textFieldName.getText();
    String description = textAreaDescription.getText();
    String category = (String) comboBox1.getSelectedItem();

    List<String> allergens = new ArrayList<>();

    for (JCheckBox box : checkboxes_) {
      if (box.isSelected()) {
        allergens.add(box.getText());
      }
    }

    try {
      double price = Double.valueOf(textFieldPrice.getText());
      Pattern regEx = Pattern.compile("\\d{1,3}(?:.\\d{0,2})?");
      Matcher matcher = regEx.matcher(textFieldPrice.getText());

      if (!matcher.matches()) {
        throw new NumberFormatException();
      }

      if (controller_.finishInsertingDish(name, description, price, category, allergens)) {
        parentFrame_.setVisible(true);
        dispose();
      }

    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(new JFrame(), "Podano nieprawidłowo cenę!",
              "Cena", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  private void createUIComponents() {
    allergensPanel = new JPanel();
    allergensPanel.setLayout(new BoxLayout(allergensPanel, BoxLayout.Y_AXIS));
  }
}
