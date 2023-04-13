package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.classes.User;
import server.exceptions.PermissionDeniedException;

public class BroadcastCommand extends Command {

    String message;
    public BroadcastCommand(User runningUser, String message) {
        super(runningUser);
        this.message = message;
    }

    @Override
    public boolean requireAdminPrivileges() {
        return true;
    }

    @Override
    public String getCommand() {
        return "bc";
    }

    @Override
    public String getHelpText() {
        return "Sends a message to everyone (without the saying that it came from a user)";
    }

    @Override
    public String getUsageText() {
        return "/bc <message>";
    }

    @Override
    public void execute() throws PermissionDeniedException {
        if (!runningUser.isAdmin()){
            throw new PermissionDeniedException(runningUser);
        }
        if (message == null) {
            ChatControl.sendMessageToUser(runningUser, "You need to specify a message");
        }
        ChatControl.sendMessageToEveryone(message);
    }
}
