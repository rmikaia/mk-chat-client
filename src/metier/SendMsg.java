/**
 * @author RALAIVITA Jonathan Mikaia
 */
package metier;

import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.text.BadLocationException;
import presentation.MyChatTextPane;

public class SendMsg {

  ObjectOutputStream out;
  String sender;
  Message msg;
  MyChatTextPane discussionThread;

  public SendMsg(
    ObjectOutputStream out,
    MyChatTextPane discussionThread,
    Message msg
  ) {
    this.out = out;
    this.sender = msg.getSenderPseudo();
    this.msg = msg;
    this.discussionThread = discussionThread;
  }

  public void forwardMsg() {
    try {
      this.out.writeObject(this.msg);
      this.out.flush();

      try {
        discussionThread
          .getDoc()
          .insertString(
            discussionThread.getDoc().getLength(),
            "\n" + this.sender,
            discussionThread.getBold()
          );

        discussionThread
          .getDoc()
          .insertString(
            discussionThread.getDoc().getLength(),
            ": " + this.msg.getMsg(),
            discussionThread.getRegular()
          );

        if (
          this.msg.getMsgType() == 5
        ) discussionThread
          .getDoc()
          .insertString(
            discussionThread.getDoc().getLength(),
            " (Message privé pour '" + this.msg.getReceveirPseudo() + "') ",
            discussionThread.getBold()
          );
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
