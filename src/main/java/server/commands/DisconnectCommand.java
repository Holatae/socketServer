package server.commands;

import server.abstracts.Command;
import server.administration.UserAdministration;
import server.classes.User;
import server.exceptions.PermissionDeniedException;
import server.exceptions.UserNotFoundException;

import java.io.IOException;

public class DisconnectCommand extends Command {
    public DisconnectCommand(User runningUser) {
        super(runningUser);
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return "dc";
    }

    @Override
    public String getHelpText() {
        return "Disconnects you from the server";
    }

    @Override
    public String getUsageText() {
        return "/dc";
    }

    @Override
    public void execute() throws PermissionDeniedException {
        try {
            runningUser.getSocket().close();
            UserAdministration.deleteUser(runningUser);
        } catch (UserNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
