package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.classes.User;
import server.exceptions.PermissionDeniedException;

public class AdminCommand extends Command {
    private User userToMakeAdmin;
    public AdminCommand(User runningUser, User userToMakeAdmin) {
        super(runningUser);
        this.userToMakeAdmin = userToMakeAdmin;
    }

    @Override
    public boolean requireAdminPrivileges() {
        return true;
    }

    @Override
    public String getCommand() {
        return "admin";
    }

    @Override
    public String getHelpText() {
        return "Makes a user an admin\n";
    }

    @Override
    public String getUsageText() {
        return "/admin [username]";
    }

    @Override
    public void execute() throws PermissionDeniedException {
        if (!runningUser.isAdmin()){throw new PermissionDeniedException(runningUser);}
        if (userToMakeAdmin == null) {
            ChatControl.sendMessageToUser(runningUser, "No user specified or user does not exist");
            return;
        }
        userToMakeAdmin.setAdmin(true);
        ChatControl.sendMessageToUser(runningUser, userToMakeAdmin.getName() + ": Is now an Admin");
    }
}
