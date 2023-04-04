package administration;

import classes.User;
import exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserAdministration {
    private static final List<User> users = Collections.synchronizedList(new ArrayList<>());

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
        if (user == null) {
            return;
        }
        try {
            synchronized (users){
                users.remove(user);
            }
        }catch (Exception e){
            throw new UserNotFoundException();
        }
    }

    public static void kickAllUsers() {
        ArrayList<User> usersToRemove = new ArrayList<>(UserAdministration.getUsers());
        synchronized (users){
            for (User user : users) {
                try {
                    ChatControl.sendMessageToUser(user, "You have been kicked from the server");
                    user.getSocket().close();
                    usersToRemove.add(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (User user : usersToRemove) {
                    users.remove(user);
            }
        }
    }
}
