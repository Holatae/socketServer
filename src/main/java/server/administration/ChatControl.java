package server.administration;

import server.classes.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

public class ChatControl {
    public synchronized static void sendMessageToUser(User user, String message) {
        PrintWriter out;
        if (user == null) {
            System.out.println(message);
            return;
        }

        try {
            if (user.getSocket() == null) {
                System.out.println(message);
                return;
            }
            out = new PrintWriter(user.getSocket().getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String encodedMessage = Base64.getEncoder().encodeToString(message.getBytes());
        out.print(encodedMessage);
        out.flush();
    }

    @SuppressWarnings("unused")
    public synchronized static void sendMessageToEveryone(String message){
        UserAdministration.getUsers().forEach(user -> sendMessageToUser(user, message));
    }
}
