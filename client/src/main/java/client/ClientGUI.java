package client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;

public class ClientGUI {
    JFrame frame = new JFrame("Chat");
    JTextPane chatFieldTextArea = new JTextPane();
    JTextField sendMessageField = new JTextField();
    JButton button = new JButton("Send");
    JScrollPane scrollPane = new JScrollPane(chatFieldTextArea);
    private String HOST = "127.0.0.1";
    private boolean haveSentName = false;
    private int PORT = 1235;

    private String messageLog = "";

    private Socket serverSocket;
    private boolean isConnected = false;

    JPanel panel = new JPanel();

    public static void main(String[] args) {
        ClientGUI clientGUI = new ClientGUI();
        clientGUI.startGUI();
    }

    private void connectToServer() {
        try {
            serverSocket = new Socket(HOST, PORT);
            System.out.println("Connected to server");
            chatFieldTextArea.setText("Connected to server\n");
            isConnected = true;
            new Thread(() -> {
                while (true){
                    while (isConnected) {
                        checkForMessages(serverSocket);
                    }
                }
            }).start();

        } catch (IOException e) {
            System.out.println("No server found");
            button.setText("Connect");
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void checkForMessages(Socket socket) {
        // Check for messages from server
        try {
            InputStream inputStream = socket.getInputStream();
            int data;
            ArrayList<Character> messageArr = new ArrayList<>();
            while (inputStream.available() > 0) {
                data = inputStream.read();
                messageArr.add((char) data);
                //String message = messageArr.stream().map(Object::toString).collect(Collectors.joining());
            }
            if (messageArr.size() > 0) {
                String encodedMessage = buildStringFromChars(messageArr);
                String message = new String(Base64.getDecoder().decode(encodedMessage));
                if (message.equals("You have been kicked from the server")) {
                    System.out.println("You have been kicked from the server");
                    chatFieldTextArea.setText("You have been kicked from the server");
                    button.setText("Connect");
                    haveSentName = false;
                    serverSocket.close();
                    return;
                }
                if (message.contains(":")) {
                    String[] messageSplitted = message.split(": ");
                    messageSplitted[0] = " <strong>" + messageSplitted[0] + "</strong>" + ": ";
                    message = String.join("", messageSplitted);

                }
                messageLog += message + " <br>";
                chatFieldTextArea.setText(messageLog);

                //chatFieldTextArea.setText(message + "\n"); //append
            }

        } catch (IOException e) {
            System.out.println("Closed connection to server");
            isConnected = false;
        }
    }
    private String buildStringFromChars(ArrayList<Character> chars) {
        StringBuilder message = new StringBuilder();
        for (Character character : chars
        ) {
            message.append(character);
        }
        return message.toString();
    }

    private void sendMessageToServer(String message, Socket socket) {
        // Send message to server
        try {
            String encodedMessage = Base64.getEncoder().encodeToString(message.getBytes());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.print(encodedMessage);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNameToServer(String name, Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.print(name);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startGUI() {
        panel.setLayout(new BorderLayout());
        chatFieldTextArea.setBounds(100, 300, 200, 100);
        button.setBounds(200, 410, 200, 90);
        sendMessageField.setBounds(400, 300, 100, 100);
        chatFieldTextArea.setEditable(false);
        chatFieldTextArea.setContentType("text/html");
        chatFieldTextArea.setFont(new Font("Arial Unicode MS", Font.PLAIN, 12));

        button.addActionListener(e -> {
            if (button.getText().equalsIgnoreCase("connect")) {
                changeServerDetails();
                connectToServer();
                button.setText("Send");
            } else
            {
                sendMessageAndLogIt();
            }
        });

        // When enter is pressed, send message
        sendMessageField.addActionListener(e -> sendMessageAndLogIt());


        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(sendMessageField, BorderLayout.SOUTH);
        panel.add(button, BorderLayout.EAST);

        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        connectToServer();

    }

    private void changeServerDetails() {
        String host = JOptionPane.showInputDialog("Enter host");
        String port = JOptionPane.showInputDialog("Enter port");
        if (host != null && !host.isEmpty()) {
            HOST = host;
        }
        if (port != null && !port.isEmpty()) {
            try {
                PORT = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void disconnectFromServer() {
        try {
            serverSocket.close();
            isConnected = false;
            button.setText("Connect");
            haveSentName = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessageAndLogIt() {
        if (sendMessageField.getText().equalsIgnoreCase("/dc")){
            sendMessageToServer("/dc", serverSocket);
            chatFieldTextArea.setText("Disconnected from server");
            disconnectFromServer();
            return;
        }
        if(sendMessageField.getText().equalsIgnoreCase("/clear")){
            chatFieldTextArea.setText("");
            messageLog = "";
            sendMessageField.setText("");
            return;
        }

        String message = sendMessageField.getText();
        if (!haveSentName) {
            String name = JOptionPane.showInputDialog("Enter name");
            sendNameToServer(name, serverSocket);
            frame.setTitle(name);
            haveSentName = true;
        }
        messageLog += "du: " + message + "<br>";
        chatFieldTextArea.setText(messageLog);
        //chatFieldTextArea.setText("du: " + message + "\n"); //append
        sendMessageToServer(message, serverSocket);

        sendMessageField.setText("");


    }
}
