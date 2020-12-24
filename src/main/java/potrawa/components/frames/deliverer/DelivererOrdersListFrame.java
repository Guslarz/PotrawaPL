package potrawa.components.frames.deliverer;

import potrawa.data.deliverer.DelivererOrder;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class DelivererOrdersListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonCancel;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final DelivererOrdersListController controller_;

  private static final String[] ordersColumnNames_ = {
      "ID zamówienia",
      "Restauracja",
      "Adres dostawy",
      "Metoda płatności",
      "Cena",
      "Dodatkowe informacje",
      "Oznacz jako dostarczone"
  };

  public DelivererOrdersListFrame(JFrame parentFrame, Connection connection) {
    super("Zamówienia dostawcy");

    parentFrame_ = parentFrame;
    controller_ = new DelivererOrdersListController(connection);

    List<DelivererOrder> orders = controller_.getOrders();

    if (orders == null || orders.size() == 0) {
      addPlaceholder();
    } else {
      addTable(orders);
    }

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);

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

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak zamówień do wyświetlenia");
    mainPanel.add(label);
  }

  private void addTable(List<DelivererOrder> orders) {
    JTable table = new DelivererOrdersTable(orders);
    mainPanel.add(new JScrollPane(table));
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  private void createUIComponents() {
    mainPanel = new JPanel();
  }

  private class DelivererOrdersTable extends JTable {

    public DelivererOrdersTable(List<DelivererOrder> orders) {
      super(new DelivererOrdersTableModel(orders));
      setDefaultRenderer(
          JButton.class,
          (table, value, isSelected, hasFocus, row, column) -> (JButton) value
      );
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          onClick(e);
        }
      });
    }

    public void onClick(MouseEvent e) {
      int row = rowAtPoint(e.getPoint());
      int column = columnAtPoint(e.getPoint());

      if (column == 6) {
        JButton button = (JButton) getValueAt(row, column);
        button.doClick();
      }
    }
  }

  private class DelivererOrdersTableModel extends AbstractTableModel {

    private final List<DelivererOrder> orders_;

    public DelivererOrdersTableModel(List<DelivererOrder> orders) {
      super();
      orders_ = orders;
    }

    @Override
    public int getRowCount() {
      return orders_.size();
    }

    @Override
    public int getColumnCount() {
      return ordersColumnNames_.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      DelivererOrder order = orders_.get(rowIndex);

      switch (columnIndex) {
        case 0:
          return Integer.toString(order.getId());
        case 1:
          return order.getRestaurantName();
        case 2:
          return order.getAddress();
        case 3:
          return order.getPaymentMethod();
        case 4:
          return String.format("%.2f", order.getPrice());
        case 5:
          return order.getAdditionalInformation();
        case 6:
          JButton button = new JButton(ordersColumnNames_[columnIndex]);
          button.addActionListener(e -> {
            controller_.confirmDelivery(order.getId());
            orders_.remove(rowIndex);
            fireTableDataChanged();
          });
          return button;
        default:
          return "Error";
      }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      if (columnIndex == 6) {
        return JButton.class;
      } else {
        return String.class;
      }
    }

    @Override
    public String getColumnName(int column) {
      return ordersColumnNames_[column];
    }
  }
}
