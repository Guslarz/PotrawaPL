package potrawa.components.frames.restaurant;

import potrawa.components.elements.OpinionElement;
import potrawa.data.Opinion;
import potrawa.logic.restaurant.RestaurantOpinionsListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class RestaurantOpinionsListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonCancel;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final RestaurantOpinionsListController controller_;

  public RestaurantOpinionsListFrame(JFrame parentFrame, Connection connection) {
    parentFrame_ = parentFrame;
    controller_ = new RestaurantOpinionsListController(connection);

    loadOpinions();

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

  private void loadOpinions() {
    mainPanel.removeAll();

    List<Opinion> opinions = controller_.getOpinions();
    if (opinions == null || opinions.size() == 0) {
      addPlaceholder();
    } else {
      addList(opinions);
    }

    pack();
    setLocationRelativeTo(null);
  }

  private void addPlaceholder() {
    JLabel label = new JLabel("Brak opinii do wyświetlenia");
    mainPanel.add(label);
  }

  private void addList(List<Opinion> opinions) {
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

    for (Opinion opinion : opinions) {
      JPanel opinionElement = new OpinionElement(opinion);
      listPanel.add(opinionElement);
    }

    listPanel.add(new Box.Filler(new Dimension(0, 0),
            new Dimension(0, 500),
            new Dimension(0, 500)));

    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 500));
    mainPanel.add(scrollPane);
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }

  @Override
  public void setVisible(boolean b) {
    if (b) {
      loadOpinions();
    }

    super.setVisible(b);
  }

  private void createUIComponents() {
    mainPanel = new JPanel();
  }
}
