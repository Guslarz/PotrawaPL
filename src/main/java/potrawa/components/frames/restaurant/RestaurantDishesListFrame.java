package potrawa.components.frames.restaurant;

import potrawa.components.elements.DishElement;
import potrawa.data.Dish;
import potrawa.logic.restaurant.RestaurantDishesListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class RestaurantDishesListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonCancel;
  private JButton buttonNewDish;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final RestaurantDishesListController controller_;
  private final Connection connection_;

  public RestaurantDishesListFrame(JFrame parentFrame, Connection connection) {
    super("Lista dań");

    parentFrame_ = parentFrame;
    controller_ = new RestaurantDishesListController(connection);
    connection_ = connection;

    loadDishes();

    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonCancel);

    buttonCancel.addActionListener(e -> onCancel());

    buttonNewDish.addActionListener(e -> onDish());

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(e -> onCancel(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void loadDishes() {
    mainPanel.removeAll();
    List<Dish> dishes = controller_.getDishes();

    if (dishes == null || dishes.size() == 0) {
      addPlaceholder();
    } else {
      addList(dishes);
    }
  }

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak dań do wyświetlenia");
    mainPanel.add(label);
  }

  private void addList(List<Dish> dishes) {
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    for (Dish dish : dishes) {
      JPanel dishElement = new DishElement(dish);

      JButton buttonUpdate = new JButton("Modyfikuj");
      buttonUpdate.addActionListener(e -> SwingUtilities.invokeLater(() -> {
        RestaurantDishFrame nextFrame =
                new RestaurantDishFrame(this, connection_, dish);
        setVisible(false);
        nextFrame.setVisible(true);
      }));
      JPanel updatePanel = new JPanel();
      updatePanel.add(buttonUpdate);
      dishElement.add(updatePanel);

      JButton buttonDelete = new JButton("Usuń");
      buttonDelete.addActionListener(e -> {
        if (controller_.deleteDish(dish.getName())) {
          loadDishes();
        }
      });
      JPanel deletePanel = new JPanel();
      deletePanel.add(buttonDelete);
      dishElement.add(deletePanel);

      listPanel.add(dishElement);
    }
    listPanel.add(new Box.Filler(new Dimension(0, 0),
            new Dimension(0, 500),
            new Dimension(0, 500)));

    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 500));
    mainPanel.add(scrollPane);
  }

  private void onDish() {
    SwingUtilities.invokeLater(() -> {
      JFrame nextFrame = new RestaurantDishFrame(this, connection_);
      nextFrame.setVisible(true);
    });
    setVisible(false);
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  private void createUIComponents() {
    mainPanel = new JPanel();
  }

  @Override
  public void setVisible(boolean b) {
    if (b) {
      loadDishes();
    }

    super.setVisible(b);
  }
}
