/**
 * @author RALAIVITA Jonathan Mikaia
 */

package metier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ListenFileTransfert implements Runnable {
  Socket fileSocket;
  InputStream in;
  OutputStream out;

  public ListenFileTransfert(Socket socket) {
    this.fileSocket = socket;
  }

  @Override
  public void run() {
    try {
      while (true) {
        in = fileSocket.getInputStream();

        (
          new File((new File("").getAbsolutePath()).toString() + "\\upload")
        ).mkdir();

        FileTransfert.sendFile(
          fileSocket.getInputStream(),
          new FileOutputStream(
            (new File("").getAbsolutePath()).toString() + "\\upload\\a.txt"
          ),
          true
        );
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
