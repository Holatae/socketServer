package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.administration.UserAdministration;
import server.classes.User;
import server.exceptions.PermissionDeniedException;
import server.exceptions.UserNotFoundException;

import java.io.IOException;

public class KickCommand extends Command {
    private final User userToKick;

    public KickCommand(User runningUser, User userToKick) {
        super(runningUser);
        this.userToKick = userToKick;
    }

    @Override
    public boolean requireAdminPrivileges() {
        return true;
    }

    @Override
    public String getCommand() {
        return "kick";
    }

    @Override
    public String getHelpText() {
        return "Kicks a user from the server\n";
    }

    @Override
    public String getUsageText() {
        return "kick <username>";
    }

    @Override
    public void execute() throws PermissionDeniedException {
        if (!runningUser.isAdmin()) {
            throw new PermissionDeniedException(runningUser);
        }
        if (userToKick == null) {
            ChatControl.sendMessageToUser(runningUser, "server.classes.User not found");
            return;
        }

        try {
            UserAdministration.deleteUser(userToKick);
            ChatControl.sendMessageToUser(userToKick, "You have been kicked from the server");
            userToKick.getSocket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UserNotFoundException e) {
            ChatControl.sendMessageToUser(runningUser, "server.classes.User not found");
        }
    }

}
