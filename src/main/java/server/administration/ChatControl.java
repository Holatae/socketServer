package server.administration;

import server.classes.User;

import java.io.IOException;
import java.io.PrintWriter;

public class ChatControl {
    public synchronized static void sendMessageToUser(User user, String message) {
        PrintWriter out = null;
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
        out.print(message);
        out.flush();

    }
}
