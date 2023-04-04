package server.commands;

import server.abstracts.Command;
import server.administration.ChatControl;
import server.classes.User;

public class HelpCommand extends Command {
    private AdminCommand adminCommand;
    private AdminlistCommand adminlistCommand;
    private KickCommand kickCommand;
    private RenameCommand renameCommand;
    private ViewCommand viewCommand;
    private DisconnectCommand disconnectCommand;
    private String command;
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

        adminCommand = new AdminCommand(null, null);
        adminlistCommand = new AdminlistCommand(null);
        kickCommand = new KickCommand(null, null);
        renameCommand = new RenameCommand(null, null);
        viewCommand = new ViewCommand(null, null);
        disconnectCommand = new DisconnectCommand(null);
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
                default -> ChatControl.sendMessageToUser(runningUser, "Command not found" + endLine);
            }
            return;
        }

        ChatControl.sendMessageToUser(runningUser, adminCommand.getCommand() + middleThing + adminCommand.getHelpText() + endLine);
        ChatControl.sendMessageToUser(runningUser, adminlistCommand.getCommand() + middleThing + adminlistCommand.getHelpText() + endLine);
        ChatControl.sendMessageToUser(runningUser, kickCommand.getCommand() + middleThing + kickCommand.getHelpText() + endLine);
        ChatControl.sendMessageToUser(runningUser, renameCommand.getCommand() + middleThing + renameCommand.getHelpText() + endLine);
        ChatControl.sendMessageToUser(runningUser, viewCommand.getCommand() + middleThing + viewCommand.getHelpText() + endLine);
        ChatControl.sendMessageToUser(runningUser, disconnectCommand.getCommand() + middleThing + disconnectCommand.getHelpText() + endLine);
    }
}
