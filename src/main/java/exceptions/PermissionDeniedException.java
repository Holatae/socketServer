package exceptions;

import administration.ChatControl;
import classes.User;

public class PermissionDeniedException extends Exception {
    /**
     * @param runningUser - The user that tried to run the command
     */
    public PermissionDeniedException(User runningUser) {
        ChatControl.sendMessageToUser(runningUser, "You don't have permission to run this command");
    }
}
