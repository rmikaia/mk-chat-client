/**
 * @author RALAIVITA Jonathan Mikaia
 */

package metier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import presentation.ChatFrame;
import presentation.MyChatTextPane;

public class ReceiveMsg implements Runnable {

  ObjectInputStream in;
  String sender;
  Message msg;
  MyChatTextPane discussionThread;
  ChatFrame cf;

  public ReceiveMsg(
    ObjectInputStream in,
    String sender,
    MyChatTextPane discussionThread,
    ChatFrame cf
  ) {
    this.in = in;
    this.sender = sender;
    this.discussionThread = discussionThread;
    this.cf = cf;
  }

  @Override
  public void run() {
    try {
      while (true) {
        this.msg = (Message) (this.in.readObject());

        System.out.println(
          this.sender + " get message type => " + this.msg.getMsgType()
        );

        if (!this.sender.equals(this.msg.getSenderPseudo())) {
          try {
            if (this.msg.getMsgType() == 3) {
              System.out.println("client => notify");
              discussionThread
                .getDoc()
                .insertString(
                  discussionThread.getDoc().getLength(),
                  "\n" + this.msg.getSenderPseudo() + this.msg.getMsg() + "\n",
                  discussionThread.getHighlight()
                );
            } else if (this.msg.getMsgType() == 4) {
              System.out.println("client => Closed");
              this.cf.dispose();
            } else if (this.msg.getMsgType() == 5) {
              System.out.println("client => getting MP ;)");

              discussionThread
                .getDoc()
                .insertString(
                  discussionThread.getDoc().getLength(),
                  "\n" + this.msg.getSenderPseudo(),
                  discussionThread.getBoldColored()
                );
              discussionThread
                .getDoc()
                .insertString(
                  discussionThread.getDoc().getLength(),
                  ": " + this.msg.getMsg(),
                  discussionThread.getRegular()
                );
              discussionThread
                .getDoc()
                .insertString(
                  discussionThread.getDoc().getLength(),
                  " (Message Priv�) ",
                  discussionThread.getBold()
                );
            } else if (this.msg.getMsgType() == 6) {
              System.out.println("client => fetchall users");
              StringTokenizer st = new StringTokenizer(this.msg.getMsg(), "|");
              Object tab[][] = new Object[st.countTokens()][];
              int i = 0;
              String header[] = { "pseudo" };

              DefaultTableModel model = new DefaultTableModel(header, i);
              this.cf.getUserListTable().setModel(model);
              model.getDataVector().removeAllElements();

              while (st.hasMoreTokens()) {
                String[] tabStr = new String[1];
                tabStr[0] = st.nextElement().toString();

                if (!this.sender.equals(tabStr[0])) {
                  tab[i] = tabStr;
                  model.addRow(tab[i]);
                }
                i++;
              }
            } else {
              discussionThread
                .getDoc()
                .insertString(
                  discussionThread.getDoc().getLength(),
                  "\n" + this.msg.getSenderPseudo(),
                  discussionThread.getBoldColored()
                );
              discussionThread
                .getDoc()
                .insertString(
                  discussionThread.getDoc().getLength(),
                  ": " + this.msg.getMsg(),
                  discussionThread.getRegular()
                );
            }
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
