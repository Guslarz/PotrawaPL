package potrawa.error;

import javax.swing.*;
import java.sql.SQLException;

public class DefaultSqlHandler {
  public static void handle(SQLException ex) {
    String msg;
    switch (ex.getErrorCode()) {
      case 1005:
        msg = "Podane puste hasło";
        break;
      case 1017:
        msg = "Błędny login lub hasło";
        break;
      case 1400:
        msg = "Wymagane pole nie może być puste";
        break;
      case 17002:
        msg = "Brak połączenia";
        break;
      default:
        msg = "Nieznany błąd";
        System.out.println(ex.toString());
        break;
    }
    JOptionPane.showMessageDialog(null,
        msg, "Błąd", JOptionPane.ERROR_MESSAGE);
  }
}
