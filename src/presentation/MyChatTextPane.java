/**
 * @author RALAIVITA Mikaia
 */

package presentation;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class MyChatTextPane extends JTextPane {

  private static final long serialVersionUID = 1L;
  private StyledDocument doc;
  private Style regular;
  private Style italic;
  private Style bold;
  private Style boldColored;
  private Style small;
  private Style large;
  private Style superscript;
  private Style highlight;

  public MyChatTextPane() {
    this.doc = this.getStyledDocument();
    Style def = StyleContext
      .getDefaultStyleContext()
      .getStyle(StyleContext.DEFAULT_STYLE);
    regular = this.doc.addStyle("regular", def);

    this.italic = this.doc.addStyle("italic", regular);
    StyleConstants.setItalic(italic, true);

    this.bold = this.doc.addStyle("bold", regular);
    StyleConstants.setBold(bold, true);

    this.boldColored = this.doc.addStyle("boldColored", bold);
    StyleConstants.setForeground(boldColored, Color.blue);

    this.small = this.doc.addStyle("small", regular);
    StyleConstants.setFontSize(small, 10);

    this.large = this.doc.addStyle("large", regular);
    StyleConstants.setFontSize(large, 16);

    this.superscript = this.doc.addStyle("superscript", regular);
    StyleConstants.setSuperscript(superscript, true);

    this.highlight = this.doc.addStyle("highlight", regular);
    StyleConstants.setBackground(highlight, Color.yellow);
  }
  
  public Style getBoldColored() {
    return boldColored;
  }

  public void setBoldColored(Style boldColored) {
    this.boldColored = boldColored;
  }

  public StyledDocument getDoc() {
    return doc;
  }

  public void setDoc(StyledDocument doc) {
    this.doc = doc;
  }

  public Style getRegular() {
    return regular;
  }

  public void setRegular(Style regular) {
    this.regular = regular;
  }

  public Style getItalic() {
    return italic;
  }

  public void setItalic(Style italic) {
    this.italic = italic;
  }

  public Style getBold() {
    return bold;
  }

  public void setBold(Style bold) {
    this.bold = bold;
  }

  public Style getSmall() {
    return small;
  }

  public void setSmall(Style small) {
    this.small = small;
  }

  public Style getLarge() {
    return large;
  }

  public void setLarge(Style large) {
    this.large = large;
  }

  public Style getSuperscript() {
    return superscript;
  }

  public void setSuperscript(Style superscript) {
    this.superscript = superscript;
  }

  public Style getHighlight() {
    return highlight;
  }

  public void setHighlight(Style highlight) {
    this.highlight = highlight;
  }
}
