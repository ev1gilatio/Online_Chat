package evigilatio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final ServerSocket serverSocket;
    private final List<ClientHandler> loggedUser;
    private final AuthService authService;

    public Server() {
        authService = new AuthService();
        loggedUser = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(5598);

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException("Smth is wrong in Server()");
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public  void subscribe(ClientHandler user) {
        loggedUser.add(user);
    }

    public void unsubscribe(ClientHandler user) {
        loggedUser.remove(user);
    }

    public boolean isUserLoggedIn(String login) {
        for(ClientHandler user : loggedUser) {
            if (user.getLogin().equals(login)) {
                return true;
            }
        }

        return false;
    }

    public void sendWhisper(ClientHandler from, String to, String message) {
        if (isUserLoggedIn(to)) {
            for (ClientHandler user : loggedUser) {
                if (user.getLogin().equals(to)) {
                    user.sendMessage("From " + from.getLogin() + ": " + message);
                }
            }
        } else {
            from.sendMessage("There is no such user or he is offline.");
        }
    }

    public void broadcastMessage(String outMessage) {
         for (ClientHandler user: loggedUser) {
             user.sendMessage(outMessage);
         }
    }
}
