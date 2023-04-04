import server.abstracts.Command;
import org.junit.jupiter.api.Test;
import server.CommandFactory;

import static org.junit.jupiter.api.Assertions.*;

class CommandFactoryTest {

    @Test
    void getAdminCommand() {
        Command command = CommandFactory.getCommand("admin", null);
        assertEquals("admin", command.getCommand());
    }
    @Test
    void getAdminListCommand() {
        Command command = CommandFactory.getCommand("admin-list", null);
        assertEquals("admin-list", command.getCommand());
    }
    @Test
    void getAdminRemoveCommand() {
        Command command = CommandFactory.getCommand("admin-remove", null);
        assertEquals("admin-remove", command.getCommand());
    }
    @Test
    void getDisconnectCommand() {
        Command command = CommandFactory.getCommand("dc", null);
        assertEquals("dc", command.getCommand());
    }
    @Test
    void getHelpCommand() {
        Command command = CommandFactory.getCommand("help", null);
        assertEquals("help", command.getCommand());
    }
    @Test
    void getKickAllCommand() {
        Command command = CommandFactory.getCommand("kickall", null);
        assertEquals("kickall", command.getCommand());
    }
    @Test
    void getKickCommand() {
        Command command = CommandFactory.getCommand("kick", null);
        assertEquals("kick", command.getCommand());
    }
    @Test
    void getRenameCommand() {
        Command command = CommandFactory.getCommand("rename", null);
        assertEquals("rename", command.getCommand());
    }
    @Test
    void getViewCommand() {
        Command command = CommandFactory.getCommand("view", null);
        assertEquals("view", command.getCommand());
    }
}
