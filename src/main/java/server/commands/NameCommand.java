package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.classes.User;
import server.exceptions.PermissionDeniedException;

public class NameCommand extends Command {
    public NameCommand(User runningUser) {
        super(runningUser);
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return "name";
    }

    @Override
    public String getHelpText() {
        return "Returns the name of your user";
    }

    @Override
    public String getUsageText() {
        return "/name";
    }

    @Override
    public void execute() throws PermissionDeniedException {
        ChatControl.sendMessageToUser(runningUser, runningUser.getName());
    }
}
