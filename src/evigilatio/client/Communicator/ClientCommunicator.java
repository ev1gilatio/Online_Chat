package evigilatio.client.Communicator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientCommunicator {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public ClientCommunicator() {
        try {
            socket = new Socket("localhost", 5598);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Smth is wrong in ClientCommunicator()");
        }
    }

    public void sendMessage(String outboundMessage) {
        try {
            out.writeUTF(outboundMessage);
        } catch (IOException e) {
            throw new RuntimeException("Smth is wrong in SendMessage()");
        }
    }

    public String receiveMessage() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("Smth is wrong in receiveMessage()");
        }
    }
}
