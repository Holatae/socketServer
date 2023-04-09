package server.commands;


import server.abstracts.Command;
import server.administration.ChatControl;
import server.administration.UserAdministration;
import server.classes.User;
import server.exceptions.PermissionDeniedException;

public class KickallCommand extends Command {
    public KickallCommand(User runningUser) {
        super(runningUser);
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return "kickall";
    }

    @Override
    public String getHelpText() {
        return null;
    }

    @Override
    public String getUsageText() {
        return null;
    }

    @Override
    public void execute() throws PermissionDeniedException {
        if (!runningUser.isAdmin()) {
            throw new PermissionDeniedException(runningUser);
        }
        UserAdministration.kickAllUsers();
        ChatControl.sendMessageToUser(runningUser, "Everyone was kicked;)");
    }
}
