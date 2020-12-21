package potrawa.components.standalone.new_user;

import potrawa.application.Application;
import potrawa.error.DefaultSqlHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class NewClientDialog extends JDialog {
  private final JDialog parentDialog_;
  private final Connection conn_;
  private final Runnable callback_;
  private JPanel contentPane;
  private JButton buttonSubmit;
  private JButton buttonCancel;
  private JTextField textField1;
  private JTextField textField2;
  private JTextField textField3;

  public NewClientDialog(JDialog parentDialog, Connection conn, Runnable callback) {
    super((Dialog)null);
    parentDialog_ = parentDialog;
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
              String.format("%s.Wspolne.Wstaw_Klienta(?, ?, ?);", Application.schema) +
              String.format("%s.Autoryzacja.Klient;", Application.schema) +
              "END;"
      );
      preparedStatement.setString(1, textField1.getText());
      preparedStatement.setString(2, textField2.getText());
      preparedStatement.setString(3, textField3.getText());
      preparedStatement.execute();
      preparedStatement.close();

      parentDialog_.dispose();
      dispose();
      callback_.run();
    } catch (SQLException ex) {
      DefaultSqlHandler.handle(ex);
    }
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
    parentDialog_.setVisible(true);
  }
}
