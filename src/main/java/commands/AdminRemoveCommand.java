package commands;

import abstracts.Command;
import administration.ChatControl;
import classes.User;
import exceptions.PermissionDeniedException;

public class AdminRemoveCommand extends Command {
    private User adminToRemove;
    public AdminRemoveCommand(User runningUser, User adminToRemove) {
        super(runningUser);
        this.adminToRemove = adminToRemove;
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return "admin-remove";
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
        adminToRemove.setAdmin(false);
        ChatControl.sendMessageToUser(runningUser, "Removed admin privileges from " + adminToRemove.getName());
    }
}
