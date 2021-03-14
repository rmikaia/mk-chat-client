package presentation;

/**
 * @author Mikaia Ralaivita
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import metier.Connexion;

public class ClientGUI extends JFrame {
  private static final long serialVersionUID = 1L;

  private JPanel pan1, adressePanel, portPanel, typeClientPanel, pseudoPanel, pwdPanel;
  private JLabel adresseLabel, portLabel, typeClientLabel, pseudoLabel, pwdLabel;
  private JTextField adresseText, portText, pseudoText, pwdText;
  private JComboBox<String> typeClientCombo;
  private String[] typeClient = { "Visiteur", "Membre" };
  private JButton submitClientSpec;
  private JTextArea serverStatusTextarea;
  private Container frameContent;

  public ClientGUI() {}

  public ClientGUI(String title) {
    super(title);
    this.pan1 = new JPanel();

    this.adressePanel = new JPanel();
    this.adresseLabel = new JLabel("Adresse: ");
    this.adresseText = new JTextField(50);
    this.portPanel = new JPanel();
    this.portLabel = new JLabel("Port: ");
    this.portText = new JTextField(50);
    this.typeClientPanel = new JPanel();
    this.typeClientLabel = new JLabel("Type client: ");
    this.typeClientCombo = new JComboBox<String>(this.typeClient);
    this.pseudoPanel = new JPanel();
    this.pseudoLabel = new JLabel("Utilisateur: ");
    this.pseudoText = new JTextField(50);
    this.pwdPanel = new JPanel();
    this.pwdLabel = new JLabel("Mot de passe: ");
    this.pwdText = new JTextField(15);
    this.submitClientSpec = new JButton("Se connecter");
    this.serverStatusTextarea = new JTextArea(40, 60);
    this.frameContent = this.getContentPane();

    this.setSize(700, 600);
    this.setLocationRelativeTo(null);
    this.frameContent.setBackground(Color.pink);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void loadGUI() {
    this.frameContent.setLayout(new FlowLayout());

    this.pan1.setLayout(new GridLayout(6, 1, 10, 10));
    this.frameContent.add(this.pan1);
    this.serverStatusTextarea.setVisible(false);
    this.frameContent.add(this.serverStatusTextarea);

    this.adressePanel.setLayout(new GridLayout(2, 1));
    this.adresseLabel.setPreferredSize(new Dimension(75, 10));
    this.adressePanel.add(this.adresseLabel);
    this.adressePanel.add(this.adresseText);

    this.portPanel.setLayout(new GridLayout(2, 1));
    this.portLabel.setPreferredSize(new Dimension(75, 10));
    this.portPanel.add(this.portLabel);
    this.portPanel.add(this.portText);

    this.pseudoPanel.setLayout(new GridLayout(2, 1));
    this.pseudoLabel.setPreferredSize(new Dimension(75, 10));
    this.pseudoPanel.add(this.pseudoLabel);
    this.pseudoPanel.add(this.pseudoText);

    this.pwdPanel.setLayout(new GridLayout(2, 1));
    this.pwdLabel.setPreferredSize(new Dimension(75, 10));
    this.pwdPanel.add(this.pwdLabel);
    this.pwdPanel.add(this.pwdText);
    this.pwdPanel.setVisible(false);

    this.submitClientSpec.addActionListener(new ManageButtonClick(this));

    this.adressePanel.setBackground(Color.green);
    this.portPanel.setBackground(Color.green);
    this.typeClientPanel.setBackground(Color.green);
    this.pseudoPanel.setBackground(Color.green);
    this.pwdPanel.setBackground(Color.green);

    this.pan1.add(this.adressePanel);
    this.pan1.add(this.portPanel);
    this.pan1.add(this.pseudoPanel);
    this.pan1.add(this.pwdPanel);
    this.pan1.add(this.submitClientSpec);

    this.pan1.setBackground(Color.darkGray);
  }

  /********************************************************
   *               Classe Listeners (EVENT HANDLER)
   ***************************************************/

  private class ManageComboSelect implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent event) {
      if (event.getItem().equals("Visiteur")) {
        pwdPanel.setVisible(false);
      } else {
        pwdPanel.setVisible(true);
      }
    }
  }

  private class ManageButtonClick implements ActionListener {

    ClientGUI clientGUIRef;

    public ManageButtonClick(ClientGUI ref) {
      this.clientGUIRef = ref;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      Object obj = event.getSource();

      if (obj.equals(submitClientSpec)) {
        if (adresseText.getText().equals("") || portText.getText().equals("")) {
          JOptionPane.showMessageDialog(
            null,
            "Veuillez remplir tous les champs",
            "Erreur",
            JOptionPane.WARNING_MESSAGE
          );
        } else {
          try {
            Connexion connexion = new Connexion(
              new Socket(
                adresseText.getText(),
                Integer.parseInt(portText.getText())
              ),
              pseudoText.getText(),
              this.clientGUIRef
            );
            (new Thread(connexion)).start();
          } catch (IOException e) {
            e.printStackTrace();
          }

          adresseText.setText("");
          portText.setText("");
          pseudoText.setText("");
          pwdText.setText("");
        }
      }
    }
  }

  public JPanel getPan1() {
    return pan1;
  }

  public void setPan1(JPanel pan1) {
    this.pan1 = pan1;
  }

  public JPanel getAdressePanel() {
    return adressePanel;
  }

  public void setAdressePanel(JPanel adressePanel) {
    this.adressePanel = adressePanel;
  }

  public JLabel getAdresseLabel() {
    return adresseLabel;
  }

  public void setAdresseLabel(JLabel adresseLabel) {
    this.adresseLabel = adresseLabel;
  }

  public JTextField getAdresseText() {
    return adresseText;
  }

  public void setAdresseText(JTextField adresseText) {
    this.adresseText = adresseText;
  }

  public JPanel getPortPanel() {
    return portPanel;
  }

  public void setPortPanel(JPanel portPanel) {
    this.portPanel = portPanel;
  }

  public JLabel getPortLabel() {
    return portLabel;
  }

  public void setPortLabel(JLabel portLabel) {
    this.portLabel = portLabel;
  }

  public JTextField getPortText() {
    return portText;
  }

  public void setPortText(JTextField portText) {
    this.portText = portText;
  }

  public JPanel getTypeClientPanel() {
    return typeClientPanel;
  }

  public void setTypeClientPanel(JPanel typeClientPanel) {
    this.typeClientPanel = typeClientPanel;
  }

  public JLabel getTypeClientLabel() {
    return typeClientLabel;
  }

  public void setTypeClientLabel(JLabel typeClientLabel) {
    this.typeClientLabel = typeClientLabel;
  }

  public JComboBox<String> getTypeClientCombo() {
    return typeClientCombo;
  }

  public void setTypeClientCombo(JComboBox<String> typeClientCombo) {
    this.typeClientCombo = typeClientCombo;
  }

  public JPanel getPseudoPanel() {
    return pseudoPanel;
  }

  public void setPseudoPanel(JPanel pseudoPanel) {
    this.pseudoPanel = pseudoPanel;
  }

  public JLabel getPseudoLabel() {
    return pseudoLabel;
  }

  public void setPseudoLabel(JLabel pseudoLabel) {
    this.pseudoLabel = pseudoLabel;
  }

  public JTextField getPseudoText() {
    return pseudoText;
  }

  public void setPseudoText(JTextField pseudoText) {
    this.pseudoText = pseudoText;
  }

  public JPanel getPwdPanel() {
    return pwdPanel;
  }

  public void setPwdPanel(JPanel pwdPanel) {
    this.pwdPanel = pwdPanel;
  }

  public JLabel getPwdLabel() {
    return pwdLabel;
  }

  public void setPwdLabel(JLabel pwdLabel) {
    this.pwdLabel = pwdLabel;
  }

  public JTextField getPwdText() {
    return pwdText;
  }

  public void setPwdText(JTextField pwdText) {
    this.pwdText = pwdText;
  }

  public String[] getTypeClient() {
    return typeClient;
  }

  public void setTypeClient(String[] typeClient) {
    this.typeClient = typeClient;
  }

  public JButton getSubmitClientSpec() {
    return submitClientSpec;
  }

  public void setSubmitClientSpec(JButton submitClientSpec) {
    this.submitClientSpec = submitClientSpec;
  }

  public JTextArea getServerStatusTextarea() {
    return serverStatusTextarea;
  }

  public void setServerStatusTextarea(JTextArea serverStatusTextarea) {
    this.serverStatusTextarea = serverStatusTextarea;
  }

  public Container getFrameContent() {
    return frameContent;
  }

  public void setFrameContent(Container frameContent) {
    this.frameContent = frameContent;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}
