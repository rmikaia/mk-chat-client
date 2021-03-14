/**
 * @author Mikaia Ralaivita
 */

package presentation;

public class Principale {

  public static void main(String[] args) {
    ClientGUI gui = new ClientGUI("Call home - Accueil");
    gui.loadGUI();

    gui.setVisible(true);
  }
}
