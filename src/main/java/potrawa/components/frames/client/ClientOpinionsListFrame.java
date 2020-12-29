package potrawa.components.frames.client;

import potrawa.components.elements.OpinionElement;
import potrawa.components.elements.OrderElement;
import potrawa.data.Opinion;
import potrawa.data.Order;
import potrawa.logic.client.ClientOpinionsListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class ClientOpinionsListFrame extends JFrame {
  private JPanel contentPane;
  private JButton buttonCancel;
  private JPanel mainPanel;

  private final JFrame parentFrame_;
  private final ClientOpinionsListController controller_;
  private final Connection connection_;

  public ClientOpinionsListFrame(JFrame parentFrame, Connection connection) {
    super("Lista opinii klienta");

    parentFrame_ = parentFrame;
    controller_ = new ClientOpinionsListController(connection);
    connection_ = connection;

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

  private void createUIComponents() {
    mainPanel = new JPanel();
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
    JLabel label = new JLabel("Brak opini do wyświetlenia");
    mainPanel.add(label);
  }

  private void addList(List<Opinion> opinions) {
    JPanel listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    for (Opinion opinion : opinions) {
      JPanel opinionElement = new OpinionElement(opinion);

      JButton buttonUpdate = new JButton("Modyfikuj");
      buttonUpdate.addActionListener(e -> SwingUtilities.invokeLater(() -> {
        ClientOpinionFrame nextFrame =
            new ClientOpinionFrame(this, connection_, opinion);
        nextFrame.setCallback(this::loadOpinions);
        setVisible(false);
        nextFrame.setVisible(true);
      }));
      JPanel updatePanel = new JPanel();
      updatePanel.add(buttonUpdate);
      opinionElement.add(updatePanel);

      JButton buttonDelete = new JButton("Usuń");
      buttonDelete.addActionListener(e -> {
        if (controller_.deleteOpinion(opinion.getRestaurantId())) {
          loadOpinions();
        }
      });
      JPanel deletePanel = new JPanel();
      deletePanel.add(buttonDelete);
      opinionElement.add(deletePanel);

      listPanel.add(opinionElement);
    }
    JScrollPane scrollPane = new JScrollPane(listPanel);
    scrollPane.setPreferredSize(new Dimension(500, 600));
    mainPanel.add(scrollPane);
  }

  private void onCancel() {
    parentFrame_.setVisible(true);
    dispose();
  }
}
