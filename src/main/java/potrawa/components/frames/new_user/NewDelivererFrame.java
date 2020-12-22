package potrawa.components.frames.new_user;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewDelivererFrame extends JFrame {
  private final JFrame parentFrame_;
  private final Connection conn_;
  private final Runnable callback_;
  private JPanel contentPane;
  private JButton buttonSubmit;
  private JButton buttonCancel;
  private JTextField textField1;
  private JTextField textField2;

  public NewDelivererFrame(JFrame parentFrame, Connection conn, Runnable callback) {
    parentFrame_ = parentFrame;
    conn_ = conn;
    callback_ = callback;
    setContentPane(contentPane);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    getRootPane().setDefaultButton(buttonSubmit);

    buttonSubmit.addActionListener(e -> onSubmit());

    buttonCancel.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onSubmit() {
    try {
      PreparedStatement preparedStatement = conn_.prepareStatement(
          "BEGIN " +
              "?.Wspolne.Wstaw_Dostawce(?, ?);" +
              "?.Autoryzacja.Dostawca;" +
              "END;"
      );
      preparedStatement.setString(1, Application.schema);
      preparedStatement.setString(2, textField1.getText());
      preparedStatement.setString(3, textField2.getText());
      preparedStatement.setString(4, Application.schema);
      preparedStatement.execute();
      preparedStatement.close();

      parentFrame_.dispose();
      dispose();
      callback_.run();
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
    }
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
    parentFrame_.setVisible(true);
  }
}
