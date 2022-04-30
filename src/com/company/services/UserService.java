package com.company.services;

import com.company.enums.Role;
import com.company.User;
import com.company.exeptions.AdminDeletionException;
import com.company.exeptions.HasBookException;
import com.company.exeptions.UserException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.enums.Role.ADMIN;

public class UserService {

    private String path;

    public UserService(String path) {
        this.path = path;
    }

    public User getUser(String username, String password) throws FileNotFoundException, UserException {

        for (User user : getAllUsers()) {

            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new UserException("Wrong username or password");
    }


    public ArrayList<User> getAllUsers() throws FileNotFoundException {

        File file = new File(path);
        Scanner sc = new Scanner(file);
        ArrayList<User> users = new ArrayList<>();

        while (sc.hasNextLine()) {
            String username = sc.nextLine();
            String name = sc.nextLine();
            String surname = sc.nextLine();
            String password = sc.nextLine();
            String role = sc.nextLine();
            sc.nextLine();

            users.add(new User(username, name, surname,password, Role.valueOf(role)));
        }
        return users;
    }

    public boolean isUserExists(String username) throws FileNotFoundException {

        ArrayList<User> users = getAllUsers();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) throws IOException {

        FileWriter fw = new FileWriter(path, true);
        PrintWriter writer = new PrintWriter(fw);

        writeUser(writer, user);

        writer.close();
    }

    private void rewriteAllUsers(ArrayList<User> users) throws IOException {

        FileWriter fw = new FileWriter(path);
        PrintWriter writer = new PrintWriter(fw);

        for (User user : users) {
            writeUser(writer, user);
        }

        writer.close();
    }

    public void deleteUserByUsername(String username) throws IOException, UserException, HasBookException {

        ArrayList<User> allUsers = getAllUsers();
        User userToDelete = getUserByUsername(username, allUsers);
        allUsers.remove(userToDelete);

        rewriteAllUsers(allUsers);
    }

    public void deleteAdmin(User adminUser) throws IOException, AdminDeletionException, UserException, HasBookException {

        validateAdminDeletion();
        deleteUserByUsername(adminUser.getUsername());
    }

    private void validateAdminDeletion() throws FileNotFoundException, AdminDeletionException {

        int adminCount = 0;
        for (User user : getAllUsers()) {

            if (user.getRole().equals(ADMIN)) {
                adminCount++;
            }
        }
        if (adminCount <= 1) {
            throw new AdminDeletionException("A user with ADMiN role can't be deleted because it is the only administrator in the system");
        }
    }

    // TODO: add validation from booksService
    private void validateUserHasNoBooks() throws HasBookException {

        throw new HasBookException("A user cannot be deleted because he have has taken some books.");
    }

    private User getUserByUsername(String username, ArrayList<User> allUsers) throws UserException {

        for (User user: allUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }

        throw new UserException(String.format("User with username '%s' not exist", username));
    }

    private void writeUser(PrintWriter writer, User user) {
        writer.println(user.getUsername());
        writer.println(user.getName());
        writer.println(user.getSurname());
        writer.println(user.getPassword());
        writer.println(user.getRole());
        writer.println();
    }

}
