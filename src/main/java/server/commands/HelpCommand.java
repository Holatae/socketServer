package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.classes.User;

public class HelpCommand extends Command {
    private final String command;
    public HelpCommand(User runningUser, String command) {
        super(runningUser);
        this.command = command;
    }

    @Override
    public boolean requireAdminPrivileges() {
        return false;
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelpText() {
        return "Lists all server.commands ";
    }

    @Override
    public String getUsageText() {
        return null;
    }

    @Override
    public void execute() {
        final String middleThing = " - ";
        final String endLine = "<br>\n";

        AdminCommand adminCommand = new AdminCommand(null, null);
        AdminlistCommand adminlistCommand = new AdminlistCommand(null);
        KickCommand kickCommand = new KickCommand(null, null);
        RenameCommand renameCommand = new RenameCommand(null, null);
        ViewCommand viewCommand = new ViewCommand(null, null);
        DisconnectCommand disconnectCommand = new DisconnectCommand(null);
        NameCommand nameCommand = new NameCommand(null);
        if (command != null) {
            switch (command) {
                case "admin" ->
                        ChatControl.sendMessageToUser(runningUser, adminCommand.getUsageText() + middleThing + adminCommand.getHelpText() + endLine);
                case "adminlist" ->
                        ChatControl.sendMessageToUser(runningUser, adminlistCommand.getUsageText() + middleThing + adminlistCommand.getHelpText() + endLine);
                case "kick" ->
                        ChatControl.sendMessageToUser(runningUser, kickCommand.getUsageText() + middleThing + kickCommand.getHelpText() + endLine);
                case "rename" ->
                        ChatControl.sendMessageToUser(runningUser, renameCommand.getUsageText() + middleThing + renameCommand.getHelpText() + endLine);
                case "view" ->
                        ChatControl.sendMessageToUser(runningUser, viewCommand.getUsageText() + middleThing + viewCommand.getHelpText() + endLine);
                case "dc" ->
                        ChatControl.sendMessageToUser(runningUser, disconnectCommand.getUsageText() + middleThing + disconnectCommand.getHelpText() + endLine);
                case "name" ->
                    ChatControl.sendMessageToUser(runningUser, nameCommand.getUsageText() + middleThing + nameCommand.getHelpText() + endLine);
                default -> ChatControl.sendMessageToUser(runningUser, "Command not found" + endLine);
            }
            return;
        }

        String adminCommandHelp = adminCommand.getCommand() + middleThing + adminCommand.getHelpText() + endLine;
        String adminListCommandHelp = adminlistCommand.getCommand() + middleThing + adminlistCommand.getHelpText() + endLine;
        String kickCommandHelp = kickCommand.getCommand() + middleThing + kickCommand.getHelpText() + endLine;
        String renameCommandHelp = renameCommand.getCommand() + middleThing + renameCommand.getHelpText() + endLine;
        String viewCommandHelp = viewCommand.getCommand() + middleThing + viewCommand.getHelpText() + endLine;
        String disconnectCommandHelp = disconnectCommand.getCommand() + middleThing + disconnectCommand.getHelpText() + endLine;
        String nameCommandHelp = nameCommand.getCommand() + middleThing + nameCommand.getHelpText() + endLine;

        String helpCommandMessage = adminCommandHelp + adminListCommandHelp + kickCommandHelp + renameCommandHelp + viewCommandHelp + disconnectCommandHelp + nameCommandHelp;

        ChatControl.sendMessageToUser(runningUser, helpCommandMessage);
    }
}
