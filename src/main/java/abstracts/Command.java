package abstracts;

import classes.User;
import exceptions.PermissionDeniedException;

public abstract class Command {
    protected User runningUser;
    public Command(User runningUser) {
        this.runningUser = runningUser;
    }

    /**
     * @return - If the command requires admin privileges
     */
    public abstract boolean requireAdminPrivileges();

    /**
     * @return - The command. This is what the user types to run the command
     */
    public abstract String getCommand();

    /**
     * @return - The help text for the command.
     * Example: Makes a user an admin
     */
    public abstract String getHelpText();

    /**
     * @return - The usage text for the command.
     * Example: /kick [username]
     */
    public abstract String getUsageText();

    /**
     * Executes the command
     * @throws PermissionDeniedException - If the user does not have permission to run the command
     */
    public abstract void execute() throws PermissionDeniedException;
}
