package commands;

import abstracts.Command;
import administration.ChatControl;
import classes.User;

public class UnknownCommand extends Command {
    public UnknownCommand(User user) {
        super(user);
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return null;
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
    public void execute() {
        ChatControl.sendMessageToUser(runningUser, "Unknown command");
    }
}
