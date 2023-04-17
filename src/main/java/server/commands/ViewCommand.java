package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.classes.User;
import server.exceptions.PermissionDeniedException;

import java.util.List;

public class ViewCommand extends Command {
    private final List<User> users;
    private final StringBuilder messageToSend = new StringBuilder();

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
            users.forEach(user -> messageToSend.append(user.getName()).append("<br>"));
        }
        else {
            users.forEach(user -> messageToSend.append(user.getName())
                    .append(" ")
                    .append(user.getSocket().getInetAddress())
                    .append(" ")
                    .append(user.getUuid()).append("<br>"));

            }
            if (users.isEmpty()){
                messageToSend.append("No one is connected to the server");
            }
            ChatControl.sendMessageToUser(runningUser, messageToSend.toString());
        }

    public ViewCommand(User user, List<User> users) {
        super(user);
        this.users = users;
    }
}
