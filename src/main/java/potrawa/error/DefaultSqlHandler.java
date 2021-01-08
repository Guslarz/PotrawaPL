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
      case 1:
        msg = "Rekord o takich danych już istnieje";
        break;
      case 1438:
        msg = "Wpisano wartość większą niż dozwolona";
        break;
      case 12899:
        msg = "Wpisane dane są za długie";
        break;
      case 2290:
        msg = "Cena musi być większa od 0!";
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
