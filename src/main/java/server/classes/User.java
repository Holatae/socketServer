package server.classes;

import java.net.Socket;
import java.util.UUID;

public class User {
    private Socket socket;
    private String name;
    private boolean isAdmin;
    private UUID uuid;

    public User(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public UUID getUuid() {
        return uuid;
    }
}
