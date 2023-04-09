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

public class Server {
    private Logger logger = LogManager.getLogger(Server.class);
    private boolean done = false;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    private final List<Socket> firstTimeConnectedSocket = Collections.synchronizedList(new ArrayList<>());

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        new Server().startServer();
    }

    /**
     * Starts the server
     */
    private void startServer() {
        done = false;
        // Make this on a separate thread
        try {
            serverSocket = new ServerSocket(1235 );
            Runnable checkMessagesRunnable = new Runnable() {
                @Override
                public void run() {
                    while (!done) {
                        try {
                            checkForMessages();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Runnable checkForFirstTimeUsers = new Runnable() {
                @Override
                public void run() {
                    while (!done) {
                        try {
                            getNameFromUser();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            User serverUser = new User(null, null);
            serverUser.setAdmin(true);
            Thread checkForFirstTimeUsersThread = new Thread(checkForFirstTimeUsers);
            checkForFirstTimeUsersThread.start();
            Thread checkMessagesThread = new Thread(checkMessagesRunnable);
            checkMessagesThread.start();
            Thread commandThread = new Thread(new Runnable() {
                @Override
                public void run() {
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
                }
            });
            commandThread.start();
            // Wait for client to connect
            logger.info("Wating for clients to connect");
            while (!done) {
                firstTimeConnectedSocket.add(serverSocket.accept());
                //clients.add(new HashMap<Socket, String>(){{put(serverSocket.accept(), "Client");}});
                logger.info("Client connected");
                //server.administration.ChatControl.sendMessageToUser(new server.classes.User(firstTimeConnectedSocket.get(firstTimeConnectedSocket.size() - 1), null), "<p>Enter Name</p>");
            }
        } catch (Exception e) {
            logger.fatal("Error: " + e.getMessage());
            done = true;
        }
    }

    private void stopServer() {
        try {
            done = true;
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            logger.fatal("Error: " + e.getMessage());
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
     * The first time a client connects, it sends its name to the server. This function gets the name from the client.
     * So the first data sent from the client is the name, always.
     * @throws IOException when something goes horribly wrong
     */
    private synchronized void getNameFromUser() throws IOException {
        ArrayList<Socket> socketsToRemove = new ArrayList<>();
        synchronized (firstTimeConnectedSocket) {
            for (Socket clientSocket : firstTimeConnectedSocket
            ) {
                InputStream inputStream = clientSocket.getInputStream();
                int data;
                ArrayList<Character> nameArr = new ArrayList<>();
                while (inputStream.available() > 0) {
                    data = inputStream.read();
                    nameArr.add((char) data);
                }
                if (nameArr.size() > 0) {
                    String name = buildStringFromChars(nameArr);
                    socketsToRemove.add(clientSocket);
                    UserAdministration.addUser(new User(clientSocket, name));
                    //users.add(new server.classes.User(clientSocket, name));
                    sendMessageToClient(name, " has connected<br>", clientSocket);
                }
            }
            for (Socket socket : socketsToRemove
            ) {
                firstTimeConnectedSocket.remove(socket);
            }
            {

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
        for (User user : UserAdministration.getUsers()){
            try {
                InputStream inputStream = user.getSocket().getInputStream();
                int data;
                ArrayList<Character> messageArr = new ArrayList<>();
                while (inputStream.available() > 0) {
                    data = inputStream.read();
                    messageArr.add((char) data);
                }
                if (messageArr.size() > 0) {
                    String encodedMessage = buildStringFromChars(messageArr);
                    String message = new String(Base64.getDecoder().decode(encodedMessage));

                    // Get the first character of the message
                    if (message.charAt(0) == '/'){
                        // Remove the first character
                        message = message.substring(1);
                        Command command = CommandFactory.getCommand(message, user);
                        command.execute();
                        return;
                    }
                    logger.info(user.getName() + ": " + message);
                    //System.out.println(user.getName() + ": " + message);
                    sendMessageToClient(user.getName(), message, user.getSocket());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (PermissionDeniedException ignored) {}
        }
    }

    /**
     * @param chars
     * @return a String from the arrayList of characters.
     */
    private String buildStringFromChars(ArrayList<Character> chars){
        StringBuilder message = new StringBuilder();
        for (Character character : chars
        ) {
            message.append(character);
        }
        return message.toString();
    }

}
