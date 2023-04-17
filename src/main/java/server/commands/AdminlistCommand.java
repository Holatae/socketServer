package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.administration.UserAdministration;
import server.classes.User;

public class AdminlistCommand extends Command {
    private final StringBuilder messageToSend = new StringBuilder();
    public AdminlistCommand(User runningsUser) {
        super(runningsUser);
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return "admin-list";
    }

    @Override
    public String getHelpText() {
        return "Lists all admins\n";
    }

    @Override
    public String getUsageText() {
        return "/admin-list";
    }

    @Override
    public void execute() {
        for (User user: UserAdministration.getUsers()
             ) {
            if (user.isAdmin()){
                messageToSend.append(user.getName()).append("<br>");
            }
        }
        if (messageToSend.toString().equals("")){
            messageToSend.append("No admins are connected to the server");
        }
        ChatControl.sendMessageToUser(runningUser, messageToSend.toString());
    }
}
