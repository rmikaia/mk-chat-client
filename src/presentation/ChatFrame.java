/**
 * @author RALAIVITA Mikaia Ralaivita
 */
package presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import metier.FileTransfert;
import metier.Grid;
import metier.ListenFileTransfert;
import metier.Message;
import metier.ReceiveMsg;
import metier.SendMsg;

public class ChatFrame extends JFrame {
  private JTextArea messageText;
  private JTextField fileText;
  private MyChatTextPane discussionThread;
  private JPanel msgPanel, buttonContent, filePanel;
  private JButton broadcastSubmit, unicastSubmit, uploadFileBtn, sendFileBtn;
  private String pseudo;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private Grid userListTable;
  private JMenuBar menuBar;
  private JMenu fichier, aide;
  private JMenuItem refreshUserList, deconnectUser, aPropos, author;
  private JFileChooser fileChooser;
  private Socket socket;
  private Socket fileSocket;
  static String currentUser = "";
  static File file;

  private static final long serialVersionUID = 1L;

  public ChatFrame(String title, Socket socket, String pseudo) {
    super(title + " (" + pseudo + ")");
    this.socket = socket;
    this.pseudo = pseudo;

    Object[][] data = {};
    String header[] = { "Pseudo" };
    this.userListTable = new Grid(data, header);
    this.userListTable.getSelectionModel()
      .addListSelectionListener(new ManageGridItemSelection(this));
    this.userListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    this.setLayout(new BorderLayout(5, 5));
    this.discussionThread = new MyChatTextPane();
    this.messageText = new JTextArea(2, 10);
    this.fileText = new JTextField(15);
    this.msgPanel = new JPanel();
    this.buttonContent = new JPanel();
    this.filePanel = new JPanel();

    this.fileChooser = new JFileChooser();

    this.broadcastSubmit = new JButton("Broadcast");
    this.unicastSubmit = new JButton("MP");
    this.uploadFileBtn = new JButton("Parcourir");
    this.sendFileBtn = new JButton("Envoyer");

    this.filePanel.add(this.fileText);
    this.filePanel.add(this.uploadFileBtn);
    this.filePanel.add(this.sendFileBtn);

    this.msgPanel.setLayout(new BorderLayout(2, 2));
    this.buttonContent.setLayout(new GridLayout(2, 1));

    this.broadcastSubmit.addActionListener(new ManageButtonClick(this));
    this.unicastSubmit.addActionListener(new ManageButtonClick(this));
    this.uploadFileBtn.addActionListener(new ManageButtonClick(this));
    this.sendFileBtn.addActionListener(new ManageButtonClick(this));

    this.buttonContent.add(this.broadcastSubmit);
    this.buttonContent.add(this.unicastSubmit);
    this.msgPanel.add(new JScrollPane(this.messageText), BorderLayout.CENTER);
    this.msgPanel.add(this.buttonContent, BorderLayout.EAST);
    this.msgPanel.add(this.filePanel, BorderLayout.SOUTH);

    this.discussionThread.setEditable(false);

    this.add(new JScrollPane(this.discussionThread), BorderLayout.CENTER);
    this.add(this.userListTable, BorderLayout.EAST);
    this.add(msgPanel, BorderLayout.SOUTH);

    this.initMenu()

    this.setSize(400, 400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    try {
      this.out = new ObjectOutputStream(this.socket.getOutputStream());
      this.in = new ObjectInputStream(this.socket.getInputStream());

      (
        new Thread(
          new ReceiveMsg(this.in, this.getPseudo(), this.discussionThread, this)
        )
      ).start()


      this.fileSocket = new Socket(this.socket.getInetAddress(), 1098);

      (new Thread(new ListenFileTransfert(this.fileSocket))).start()
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void initMenu() {
    this.menuBar = new JMenuBar();
    this.fichier = new JMenu("Fichier");
    this.aide = new JMenu("?");
    this.refreshUserList = new JMenuItem("Actualiser");
    this.deconnectUser = new JMenuItem("Déconnexion");
    this.aPropos = new JMenuItem("A propos");
    this.author = new JMenuItem("Auteur");

    this.deconnectUser.addActionListener(new ManageButtonClick(this));
    this.refreshUserList.addActionListener(new ManageButtonClick(this));

    this.fichier.add(this.refreshUserList);
    this.fichier.add(this.deconnectUser);

    this.aide.add(this.aPropos);
    this.aide.add(this.author);

    this.menuBar.add(this.fichier);
    this.menuBar.add(this.aide);

    this.setJMenuBar(this.menuBar);
  }

  private class ManageButtonClick implements ActionListener {

    ChatFrame chatFrameRef;

    public ManageButtonClick(ChatFrame cf) {
      this.chatFrameRef = cf;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      Object obj = event.getSource();

      if (obj.equals(broadcastSubmit)) {
        if (!this.chatFrameRef.messageText.getText().equals("")) {
          SendMsg send = new SendMsg(
            this.chatFrameRef.out,
            this.chatFrameRef.discussionThread,
            new Message(
              this.chatFrameRef.getPseudo(),
              "all",
              this.chatFrameRef.messageText.getText(),
              2
            )
          );
          send.forwardMsg();
          this.chatFrameRef.messageText.setText("");
        }
      } else if (obj.equals(unicastSubmit)) {
        if (!ChatFrame.currentUser.equals("")) {
          if (!this.chatFrameRef.messageText.getText().equals("")) {
            SendMsg send = new SendMsg(
              this.chatFrameRef.out,
              this.chatFrameRef.discussionThread,
              new Message(
                this.chatFrameRef.getPseudo(),
                ChatFrame.currentUser,
                this.chatFrameRef.messageText.getText(),
                5
              )
            );
            send.forwardMsg();
            this.chatFrameRef.messageText.setText("");
          }
        } else {
          JOptionPane.showMessageDialog(
            null,
            "Veuillez s�lectionner au moins un utilisateur pour pouvoir envoyer un Whisper Message",
            "Erreur",
            JOptionPane.WARNING_MESSAGE
          );
        }
      } else if (
        obj.equals(deconnectUser)
      ) 
        System.out.println("deconnectUser");
        try {
          this.chatFrameRef.out.writeObject(
              new Message(
                this.chatFrameRef.pseudo,
                "server",
                "Je veux partir",
                4
              )
            );
          this.chatFrameRef.out.flush();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else if (
        obj.equals(refreshUserList)
      ) 
        System.out.println("refreshUserList");
        try {
          this.chatFrameRef.out.writeObject(
              new Message(
                this.chatFrameRef.pseudo,
                "server",
                "Je veux tous les users",
                6
              )
            );
          this.chatFrameRef.out.flush();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      } else if (obj.equals(uploadFileBtn)) {
        int returnVal =
          this.chatFrameRef.fileChooser.showOpenDialog(this.chatFrameRef);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          ChatFrame.file = this.chatFrameRef.fileChooser.getSelectedFile();
          this.chatFrameRef.fileText.setText(ChatFrame.file.getAbsolutePath());
        } else {
          this.chatFrameRef.fileText.setText("");
        }
      } else if (obj.equals(sendFileBtn)) {
        try {
          FileTransfert.sendFile(
            new FileInputStream(ChatFrame.file.getAbsolutePath()),
            this.chatFrameRef.fileSocket.getOutputStream(),
            true
          );
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private class ManageGridItemSelection implements ListSelectionListener {

    ChatFrame cfRef;

    public ManageGridItemSelection(ChatFrame cf) {
      this.cfRef = cf;
    }

    @Override
    public synchronized void valueChanged(ListSelectionEvent event) {
      JTable jt = this.cfRef.getUserListTable();
      ChatFrame.currentUser = (String) jt.getValueAt(jt.getSelectedRow(), 0);

      System.out.println(" Current selected user => " + ChatFrame.currentUser);
    }
  }

  public JMenuItem getRefreshUserList() {
    return refreshUserList;
  }

  public void setRefreshUserList(JMenuItem refreshUserList) {
    this.refreshUserList = refreshUserList;
  }

  public ObjectOutputStream getOut() {
    return out;
  }

  public void setOut(ObjectOutputStream out) {
    this.out = out;
  }

  public JTable getUserListTable() {
    return userListTable;
  }

  public void setUserListTable(Grid userListTable) {
    this.userListTable = userListTable;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public String getPseudo() {
    return pseudo;
  }

  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  public MyChatTextPane getDiscussionThread() {
    return discussionThread;
  }

  public void setDiscussionThread(MyChatTextPane discussionThread) {
    this.discussionThread = discussionThread;
  }

  public JTextArea getMessageText() {
    return messageText;
  }

  public void setMessageText(JTextArea messageText) {
    this.messageText = messageText;
  }

  public JPanel getMsgPanel() {
    return msgPanel;
  }

  public void setMsgPanel(JPanel msgPanel) {
    this.msgPanel = msgPanel;
  }

  public JPanel getButtonContent() {
    return buttonContent;
  }

  public void setButtonContent(JPanel buttonContent) {
    this.buttonContent = buttonContent;
  }

  public JButton getBroadcastSubmit() {
    return broadcastSubmit;
  }

  public void setBroadcastSubmit(JButton broadcastSubmit) {
    this.broadcastSubmit = broadcastSubmit;
  }

  public JButton getUnicastSubmit() {
    return unicastSubmit;
  }

  public void setUnicastSubmit(JButton unicastSubmit) {
    this.unicastSubmit = unicastSubmit;
  }
}
