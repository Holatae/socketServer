package commands;

import abstracts.Command;
import administration.ChatControl;
import administration.UserAdministration;
import classes.User;

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
