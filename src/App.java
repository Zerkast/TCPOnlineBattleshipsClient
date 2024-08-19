import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import application.Controller;
import application.Model;
import application.ReceiverProtocolManager;
import application.SenderProtocolManager;
import application.View;
import net.*;


public class App {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("127.0.0.1", 60000);
        PrintWriter out = new PrintWriter(s.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));       
        Sender sender = new Sender(out);
        SenderProtocolManager spm = new SenderProtocolManager(sender);
        Model app = new Model(spm);
        app.setObserver(new View(new Controller(app)));
        ReceiverProtocolManager receiver = new ReceiverProtocolManager(app, spm);
        Receiver reader=new Receiver("quit", in);
        reader.setConsumer(receiver);
    }
}
