package commands;


import abstracts.Command;
import administration.UserAdministration;
import classes.User;
import exceptions.PermissionDeniedException;

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
    }
}
