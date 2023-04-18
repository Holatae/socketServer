package server.administration;

import server.classes.User;
import server.exceptions.UserNotFoundException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserAdministration {
    private static final List<User> users = new CopyOnWriteArrayList<>();

    public static List<User> getUsers() {
        synchronized (users){
            return users;
        }
    }

    public static void addUser(User user){
        synchronized (users){
            users.add(user);
        }
    }

    /**
     * @deprecated Use {@link #getUserByEitherNameOrUuid(String)} instead
     * @param name the name of the user to find
     * @return the user with the given name, or null if no user with that name exists
     */
    public static User getUserByName(String name){
        synchronized (users){
            for (User user : users) {
                if (user.getName().equalsIgnoreCase(name)){
                    return user;
                }
            }
        }
        return null;
    }

    public static User getUserByEitherNameOrUuid(String nameOrUuid){
        synchronized (users){
            for (User user : users) {
                if (user.getName().equalsIgnoreCase(nameOrUuid) || user.getUuid().toString().equals(nameOrUuid)){
                    return user;
                }
            }
        }
        return null;
    }


    /**
     * @deprecated Use {@link #getUserByEitherNameOrUuid(String)} instead
     * @param uuid the uuid of the user to find
     * @return the user with the given uuid, or null if no user with that uuid exists
     */
    public static User getUserByUuid(UUID uuid){
        synchronized (users){
            for (User user : users) {
                if (user.getUuid().toString().equals(uuid.toString())){
                    return user;
                }
            }
        }
        return null;
    }

    public synchronized static void deleteUser(User user) throws UserNotFoundException {
        try {
            user.getSocket().close();
            users.removeIf(users -> true);
        }
        catch (Exception e){
            throw new UserNotFoundException();
        }
    }

    public static void kickAllUsers() {
        for (User user : users) {
            try {
                ChatControl.sendMessageToUser(user, "You have been kicked from the server");
                user.getSocket().close();
                deleteUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
