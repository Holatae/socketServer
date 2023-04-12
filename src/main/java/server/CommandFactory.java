package server;

import server.abstracts.Command;
import server.administration.UserAdministration;
import server.classes.User;
import server.commands.*;

public class CommandFactory {
    /**
     * This method returns the command that should be executed.
     * To run the command, call the execute() method.
     * @param command The command to be executed as a string
     * @param user The user that executes the command
     * @return The command that should be executed
     */
    public static Command getCommand(String command, User user) {
        String[] commandArr = command.split(" ");
        try{
            return switch (commandArr[0]) {
                case "view" -> new ViewCommand(user, UserAdministration.getUsers());
                case "help" -> {
                    if (commandArr.length == 1)
                        yield new HelpCommand(user, null);
                    else
                        yield new HelpCommand(user, commandArr[1]);
                }
                case "kick" -> {
                    if (commandArr.length == 1)
                        yield new KickCommand(user, null);
                    else
                        yield new KickCommand(user, UserAdministration.getUserByEitherNameOrUuid(commandArr[1]));
                }
                case "rename" -> {
                    if (commandArr.length == 1)
                        yield new RenameCommand(user, null);
                    else
                        yield new RenameCommand(user, commandArr[1]);
                }
                case "admin" -> {
                    if (commandArr.length == 1)
                        yield new AdminCommand(user, null);
                    else
                        yield new AdminCommand(user, UserAdministration.getUserByEitherNameOrUuid(commandArr[1]));
                }
                case "admin-list" -> new AdminlistCommand(user);
                case "admin-remove" -> {
                    if (commandArr.length == 1)
                        yield new AdminRemoveCommand(user, null);
                    else
                        yield new AdminRemoveCommand(user, UserAdministration.getUserByEitherNameOrUuid(commandArr[1]));
                }
                case "kickall" -> new KickallCommand(user);
                case "dc" -> new DisconnectCommand(user);
                case "name" -> new NameCommand(user);
                default -> new UnknownCommand(user);
            };
        }catch (Exception e){
            return new UnknownCommand(user);
        }

    }
}
