package evigilatio.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ClientHandler {
    private final Server server;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    doAuthentication();
                    listenMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection(socket);
                }
            })
                    .start();
        } catch (IOException e) {
            throw new RuntimeException("Smth is wrong in ClientHandler()");
        }
    }

    private void closeConnection(Socket socket) {
        server.unsubscribe(this);
        server.broadcastMessage(String.format("%s disconnected.", login));

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLogin() {
        return login;
    }

    private void doAuthentication() throws IOException {
        sendMessage("Please do authentication: /auth login password");

        while (true) {
            String maybeCredentials = in.readUTF();

            if (maybeCredentials.startsWith("/auth")) {
                String[] credentials = maybeCredentials.split("\\s");

                Optional<AuthService.Entry> maybeUser = server.getAuthService()
                        .findUserByLoginAndPassword(credentials[1], credentials[2]);

                if (maybeUser.isPresent()) {
                    AuthService.Entry user = maybeUser.get();
                    login = user.getLogin();

                    if (!server.isUserLoggedIn(login)) {
                        sendMessage("AUTH OK.");
                        sendMessage("Welcome!");

                        server.broadcastMessage(String.format("%s entered chat.", login));
                        server.subscribe(this);

                        return;
                    } else {
                        sendMessage("Current user is already logged in.");
                    }
                } else {
                    sendMessage("Invalid credentials.");
                }
            } else {
                sendMessage("Invalid auth operation.");
            }
        }
    }

    public void sendMessage(String outMessage) {
        try {
            out.writeUTF(outMessage);
        } catch (IOException e) {
            throw new RuntimeException("Smth is wrong in sendMessage()");
        }
    }

    public void listenMessages() throws IOException {
        String[] words;

        while (true) {
            String inMessage = in.readUTF();

            if (inMessage.equals("/exit")) {
                break;
            }

            if (inMessage.startsWith("/whisper")) {
                words = inMessage.split("\\s");
                inMessage = "";

                for (int i = 2; i < words.length; i++) {
                    inMessage += words[i] + " ";
                }

                sendMessage("To " + words[1] + ": " + inMessage);
                server.sendWhisper(this, words[1], inMessage);
            } else {
                server.broadcastMessage(login + ": " + inMessage);
            }
        }
    }
}
