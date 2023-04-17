package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.abstracts.Command;
import server.administration.UserAdministration;
import server.classes.User;
import server.exceptions.PermissionDeniedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private final Logger logger = LogManager.getLogger(Server.class);
    private boolean done = false;


    public static void main(String[] args) {
        new Server().startServer();
    }

    /**
     * Starts the server
     */
    private void startServer() {
        done = false;
        // Make this on a separate thread
        try (ServerSocket serverSocket = new ServerSocket(1235)){
            Runnable checkMessagesRunnable = () -> {
                while (!done) {
                    try {
                        checkForMessages();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            User serverUser = new User(null, null);
            serverUser.setAdmin(true);

            Thread checkMessagesThread = new Thread(checkMessagesRunnable);
            checkMessagesThread.start();

            Thread commandThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (!done) {
                    String commandFromServer = scanner.nextLine();
                    Command command = CommandFactory.getCommand(commandFromServer, serverUser);
                    try {
                        command.execute();
                    } catch (PermissionDeniedException e) {
                        e.printStackTrace();
                    }
                }
            });
            commandThread.start();
            // Wait for client to connect
            logger.info("Waiting for clients to connect");
            while (!done) {
                UserAdministration.addUser(new User(serverSocket.accept(), null));
                logger.info("Client connected from " + UserAdministration.getUsers()
                        .get(UserAdministration.getUsers().size() - 1).getSocket().getInetAddress());
            }
        } catch (Exception e) {
            logger.fatal("Error: " + e.getMessage());
            done = true;
        }
    }


    /**
     * @param sendingUser - The user that sent the message
     * @param message - The message that was sent
     * @param currentClientSocket - The socket of the client that should not get the message
     */
    private void sendMessageToClient(String sendingUser, String message, Socket currentClientSocket) {
        synchronized (UserAdministration.getUsers()){
            for (User user : UserAdministration.getUsers()
            ) {
                // Send message to client
                try {
                    if (user.getSocket() != currentClientSocket) {
                        PrintWriter out = new PrintWriter(user.getSocket().getOutputStream(), true);
                        String messageToSend = sendingUser + ": "+ message;
                        String encodedMessage = Base64.getEncoder().encodeToString(messageToSend.getBytes());
                        out.print(encodedMessage);
                        out.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Checks for messages from clients. This function runs on its own thread
     * @throws IOException when something goes horribly wrong
     */
    //Check for messages from clients
    private synchronized void checkForMessages() throws IOException {
        // Check for messages from clients
        List<User> users = UserAdministration.getUsers();
        for (User user : users){
            try {
                InputStream inputStream = user.getSocket().getInputStream();
                int data;
                ArrayList<Character> messageArr = new ArrayList<>();
                while (inputStream.available() > 0) {
                    data = inputStream.read();
                    messageArr.add((char) data);
                }
                if (messageArr.size() > 0) {
                    String encodedMessage = messageArr.stream()
                            .map(Objects::toString)
                            .collect(Collectors.joining());
                    String message = new String(Base64.getDecoder().decode(encodedMessage));

                    //if the User hasn't sent their name
                    if (!user.isNameSent()){
                        if (message.contains(" ")) {
                            user.setName(message.split(" ")[0]);
                        } else {
                            user.setName(message);
                        }
                        user.setNameSent(true);
                        return;
                    }

                    // Get the first character of the message
                    if (message.charAt(0) == '/'){
                        // Remove the first character
                        message = message.substring(1);
                        Command command = CommandFactory.getCommand(message, user);
                        command.execute();
                        logger.info(user.getName() + " executed " + command.getCommand());
                        return;
                    }
                    logger.info(user.getName() + ": " + message);
                    //System.out.println(user.getName() + ": " + message);
                    sendMessageToClient(user.getName(), message, user.getSocket());
                }
            } catch (IOException e) {
                logger.warn("User " + user.getName() + " has probably disconnected");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (PermissionDeniedException ignored) {}
        }
    }
}
