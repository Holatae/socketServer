package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.administration.UserAdministration;
import server.classes.User;

public class AdminlistCommand extends Command {
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
                ChatControl.sendMessageToUser(user, user.getName());
            }
        }
    }
}
