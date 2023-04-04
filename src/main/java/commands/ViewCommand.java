package commands;

import abstracts.Command;
import administration.ChatControl;
import classes.User;
import exceptions.PermissionDeniedException;

import java.util.List;

public class ViewCommand extends Command {
    private final List<User> users;

    @Override
    public boolean requireAdminPrivileges() {
        return true;
    }

    @Override
    public String getCommand() {
        return "view";
    }

    @Override
    public String getHelpText() {
        return "Lists all users\n";
    }

    @Override
    public String getUsageText() {
        return "/view";
    }

    @Override
    public void execute() throws PermissionDeniedException {
        if (!runningUser.isAdmin()) {
            for (User user : users) {
                {
                    ChatControl.sendMessageToUser(runningUser,
                            user.getName() + "<br>");
                }
            }

        }
        else {
            for (User user : users) {
                ChatControl.sendMessageToUser(runningUser,
                        user.getName() + " " +
                                user.getSocket().getInetAddress() + " " +
                                user.getUuid() + "<br>");
            }
        }
    }

    public ViewCommand(User user, List<User> users) {
        super(user);
        this.users = users;
    }
}
