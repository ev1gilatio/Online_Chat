package evigilatio.client;

import evigilatio.client.Communicator.ClientCommunicator;
import evigilatio.client.GUI.Frame;

import java.util.function.Consumer;

public class Client {
    private final Frame frame;
    private final ClientCommunicator communicator;

    public Client() {
        communicator = new ClientCommunicator();

        Consumer<String> outMessageConsumer = new Consumer<String>() {
            @Override
            public void accept(String outMessage) {
                communicator.sendMessage(outMessage);
            }
        };

        frame = new Frame(outMessageConsumer);

        new Thread(() -> {
            while (true) {
                String inMessage = communicator.receiveMessage();
                frame.getInMessageConsumer().accept(inMessage);
            }
        }).start();
    }
}
