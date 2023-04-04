package server.commands;

import server.abstracts.Command;
import server.classes.User;

public class RenameCommand extends Command {
    private final String nameToChangeTo;
    public RenameCommand(User runningUser, String nameToChangeTo) {
        super(runningUser);
        this.nameToChangeTo = nameToChangeTo;
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return "rename";
    }

    @Override
    public String getHelpText() {
        return "Changes your name\n";
    }

    @Override
    public String getUsageText() {
        return "rename <new name>";
    }

    @Override
    public void execute() {
        runningUser.setName(nameToChangeTo);
    }
}
