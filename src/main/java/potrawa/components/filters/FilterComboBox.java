package potrawa.components.filters;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class FilterComboBox extends JComboBox {
  private List<String> array;

  public FilterComboBox(List<String> array) {
    this.array = array;
    this.setEditable(true);
    final JTextField textField = (JTextField) this.getEditor().getEditorComponent();
    textField.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent ke) {
        SwingUtilities.invokeLater(() -> comboFilter(textField.getText()));
      }
    });
  }

  public void comboFilter(String enteredText) {
    if (!this.isPopupVisible()) {
      this.showPopup();
    }

    List<String> filterArray = new ArrayList<>();
    for (int i = 0; i < array.size(); i++) {
      if (array.get(i).toLowerCase().contains(enteredText.toLowerCase())) {
        filterArray.add(array.get(i));
      }
    }

    if (filterArray.size() > 0) {
      DefaultComboBoxModel model = (DefaultComboBoxModel) this.getModel();
      model.removeAllElements();
      for (String s : filterArray)
        model.addElement(s);

      JTextField textField = (JTextField) this.getEditor().getEditorComponent();
      textField.setText(enteredText);
    }
  }
}
