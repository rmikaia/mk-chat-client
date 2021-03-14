/**
 *	@author RALAIVITA Mikaia
 */
package metier;

import javax.swing.JTable;

public class Grid extends JTable {
  private static final long serialVersionUID = 1L;

  public Grid(Object[][] arg0, Object[] arg1) {
    super(arg0, arg1);
  }
  
  @Override
  public boolean isCellEditable(int arg0, int arg1) {
    return false;
  }
}
