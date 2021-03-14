/**
 * @author RALAIVITA Jonathan Mikaia
 */
package metier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import presentation.ChatFrame;
import presentation.ClientGUI;

public class Connexion implements Runnable {
  private Socket socket;
  private String pseudo;
  private String pass;
  private BufferedReader in;
  private ObjectOutputStream outObj;
  private boolean connected = false;
  ClientGUI clientGUIRef;

  public Connexion(Socket socket, String pseudo, ClientGUI clientGUIRef) {
    this.socket = socket;
    this.pseudo = pseudo;
    this.clientGUIRef = clientGUIRef;
  }

  public Connexion(Socket socket, String pseudo, String pass) {
    this.socket = socket;
    this.pseudo = pseudo;
    this.pass = pass;
  }

  @Override
  public void run() {
    try {
      this.in =
        new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      this.outObj = new ObjectOutputStream(this.socket.getOutputStream());

      while (!this.isConnected()) {
        this.outObj.writeObject(
            new Message(this.pseudo, "serveur", "Je veux me connecter", 1)
          );
        this.outObj.flush();

        String responseText = this.in.readLine();

        if (
          responseText.equals("ok")
        ) {
          this.setConnected(true);
          clientGUIRef.dispose();

          ChatFrame chatFrame = new ChatFrame(
            "Chat_o_mika",
            this.socket,
            this.pseudo
          );

          chatFrame.setVisible(true);

          try {
            chatFrame
              .getDiscussionThread()
              .getDoc()
              .insertString(
                chatFrame.getDiscussionThread().getDoc().getLength(),
                "\nBonjour " + this.pseudo + "! Bienvenue dans le chat \n",
                chatFrame.getDiscussionThread().getBold()
              );
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
        } else if (responseText.equals("nonOk")) {
          JOptionPane.showMessageDialog(
            null,
            "Pseudo déjà utilisé",
            "Erreur",
            JOptionPane.WARNING_MESSAGE
          );
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }
}
