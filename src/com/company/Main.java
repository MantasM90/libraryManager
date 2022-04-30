package com.company;

import com.company.enums.Genre;
import com.company.enums.Role;
import com.company.exeptions.AdminDeletionException;
import com.company.exeptions.BookException;
import com.company.exeptions.HasBookException;
import com.company.exeptions.UserException;
import com.company.services.BookService;
import com.company.services.ReservationService;
import com.company.services.UserService;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.enums.Genre.*;
import static com.company.enums.Role.*;

public class Main {

    private static final String UNRECOGNIZED_INPUT = "Unrecognized input";
    private static final Scanner SC = new Scanner(System.in);
    private static final String usersPath = "src/com/company/files/users.txt";
    private static final String libraryPath = "src/com/company/files/library.txt";
    private static final String reservationReportsPath = "src/com/company/files/reservationReports.txt";

    public static void main(String[] args) {

        UserService userService = new UserService(usersPath);
        BookService bookService = new BookService(libraryPath);
        ReservationService reservationService = new ReservationService(reservationReportsPath);

        Scanner SC = new Scanner(System.in);

        try {
            while (true) {

                printMenu();
                String selected = SC.nextLine();

                switch (selected) {

                    case "1":

                        System.out.print("Enter username: ");
                        String username = SC.nextLine();
                        System.out.print("Enter password: ");
                        String password = SC.nextLine();

                        try {
                            User loggedInUser = userService.getUser(username, password);
                            userMenu(loggedInUser, userService, bookService, reservationService);
                        }  catch (UserException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "2":

                        registerSimpleNewUser(userService, bookService, reservationService);
                        break;
                    case "3":

                        System.out.println("Program closed");
                        return;
                    default:

                        System.out.println(UNRECOGNIZED_INPUT);
                }
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }

    }

    private static void userMenu(User user, UserService userService, BookService bookService, ReservationService reservationService) throws IOException {

        if (user.getRole().equals(SIMPLE)) {

            simpleUserMenu(user, bookService, reservationService);
        } else if (user.getRole().equals(ADMIN)) {

            adminUserMenu(user, userService, bookService);
        }
    }

    private static void printMenu(){
        System.out.println("[1] Log in");
        System.out.println("[2] Register user (only reader)");
        System.out.println("[3] Exit");
    }

    private static void simpleUserMenu(User simpleUser, BookService bookService, ReservationService reservationService) throws FileNotFoundException {

        while (true) {
            printSimpleMenu();

            String choice = SC.nextLine();

            switch (choice) {

                case "1":
                    printSimpleUserDetails(simpleUser);
                    break;
                case "2":
                    ArrayList<Book> allBooks = null;
                    try {
                        allBooks = bookService.getAllBooks();
                        printAllBooks(allBooks);
                    } catch (IllegalArgumentException e) {
                        System.out.println(allBooks);
                    }

                    break;

                case "3":
                    Genre genre = getGenreForNewBook();
                    ArrayList<Book> allBooksByGenre = bookService.getAllBooksByGenre(genre);
                    printAllBooks(allBooksByGenre);
                    break;
                case "4":

                    break;
                case "5":

                    break;
                case "6":
                    return;
                default:
                    System.out.println(UNRECOGNIZED_INPUT);
                    break;
            }
        }
    }
    private static void printSimpleMenu() {
        System.out.println();
        System.out.println("-------READER MENU-------");
        System.out.println("[1] My details");
        System.out.println("[2] Books list");
        System.out.println("[3] Books list by genre");
        System.out.println("[4] Pick up the book");
        System.out.println("[5] Return the book");
        System.out.println("[6] Exit");
        System.out.println();
    }


    private static void adminUserMenu(User adminUser, UserService userService, BookService bookService) throws IOException {

        while (true) {

            printAdminMenu();
            String choice = SC.nextLine();

            switch (choice) {

                case "1":
                        printAdminUserDetails(adminUser);
                    break;
                case "2":
                    ArrayList<User> allUsers = userService.getAllUsers();
                    printAllUsers(allUsers);
                    break;
                case "3":
                    registerNewUser(userService);
                    break;
                case "4":
                    System.out.println("--------USER DELETION-------------");
                    System.out.print("Enter username : ");
                    String usernameToDelete = SC.nextLine();

                    try {
                        deleteUser(adminUser, userService, usernameToDelete);
                        System.out.printf("User '%s' successfully deleted.\n", usernameToDelete);

                    } catch (AdminDeletionException | UserException | HasBookException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "5":

                    break;
                case "6":

                    break;
                case "7":
                    ArrayList<Book> allBooks = bookService.getAllBooks();
                    printAllBooks(allBooks);
                    break;
                case "8":
                    Genre genre = getGenreForNewBook();
                    ArrayList<Book> allBooksByGenre = bookService.getAllBooksByGenre(genre);
                    printAllBooks(allBooksByGenre);
                    break;
                case "9":
                    addNewBook(bookService);
                    break;
                case "10":

                     try {
                         deleteValidBook(bookService);
                     } catch (BookException | IOException e) {
                         System.out.println(e.getMessage());
                     }
                    break;
                case "11":
                    return;
                default:
                    System.out.println(UNRECOGNIZED_INPUT);
                    break;
            }
        }
    }

    private static void printAdminMenu() {
        System.out.println();
        System.out.println("------------LIBRARIAN MENU-----------------");
        System.out.println("[1] My details");
        System.out.println("[2] Show all users information");
        System.out.println("[3] Register user (reader and admin");
        System.out.println("[4] Remove user (can't delete user with books)");

        System.out.println("[5] Confirm the book selection");
        System.out.println("[6] Confirm the return of the book");
        System.out.println("[7] Books list");
        System.out.println("[8] Books list by genre");
        System.out.println("[9] Add book");
        System.out.println("[10] Remove book");
        System.out.println("[11] Exit");
        System.out.println();
    }
    private static String getValidUsername(UserService userService) throws FileNotFoundException {

        String username = "";
        while (true) {

            System.out.print("Enter username: ");
            username = SC.nextLine();
            if (!userService.isUserExists(username)) {
                return username;
            }
            System.out.printf("Username '%s' already exist.\n", username);
        }
    }

    private static String getValidBookName(BookService bookService) throws FileNotFoundException {

        String name = "";
        while (true) {

            System.out.print("Enter book name: ");
            name = SC.nextLine();
            if (!bookService.bookExists(name)) {
                return name;
            }
            System.out.printf("Book with name '%s' already exist.\n", name);
        }
    }

    private static void printAllUsers(ArrayList<User> users) {

        for (User user : users) {

            printAdminUserDetails(user);
        }
    }

    private static void printAllBooks(ArrayList<Book> books) {

        if (books.size() != 0 ) {
            for (Book book : books) {

                System.out.println("--------------------------------------");
                System.out.println("Book name: " + book.getName());
                System.out.println("Genre: " + book.getGenre());
            }
            System.out.println();
        } else {
            System.out.println("Library empty");
        }
    }

    private static void printSimpleUserDetails(User simpleUser) {
        System.out.println("--------------------------------------");
        System.out.println("Username: " + simpleUser.getUsername());
        System.out.println("Name: " + simpleUser.getName());
        System.out.println("Surname: " + simpleUser.getSurname());
        System.out.println();
    }

    private static void printAdminUserDetails(User adminUser) {
        System.out.println("--------------------------------------");
        System.out.println("Username: " + adminUser.getUsername());
        System.out.println("Name: " + adminUser.getName());
        System.out.println("Surname: " + adminUser.getSurname());
        System.out.println("Role: " + adminUser.getRole());
        System.out.println();
    }

    private static void registerSimpleNewUser(UserService userService,BookService bookService ,ReservationService reservationService) throws IOException {

        System.out.println("------------NEW READER REGISTRATION------------");
        String username = getValidUsername(userService);
        System.out.print("Enter name: ");
        String name = SC.nextLine();
        System.out.print("Enter surname: ");
        String surname = SC.nextLine();
        System.out.print("Enter password: ");
        String password = SC.nextLine();

        User user = new User(username, name, surname, password);
        userService.addUser(user);
        System.out.println("User created successfully");
        simpleUserMenu(user, bookService, reservationService);
    }

    private static void deleteUser(User adminUser, UserService userService, String usernameToDelete) throws IOException, AdminDeletionException, UserException, HasBookException {

        if (!adminUser.getUsername().equalsIgnoreCase(usernameToDelete)) {
            userService.deleteUserByUsername(usernameToDelete);
            return;
        }

        System.out.println("You are trying to delete yourself, are you sure? [Y/N]");
        String deleteChoice = SC.nextLine();

        if (!(deleteChoice.equalsIgnoreCase("Y") || deleteChoice.equalsIgnoreCase("yes"))) {
            System.out.println("User deletion canceled");
            return;
        }

        // TODO: check if user has books before deletion.
        userService.deleteAdmin(adminUser);
    }

    private static void registerNewUser(UserService userService) throws IOException {

        System.out.println("------------NEW USER REGISTRATION------------");
        String username = getValidUsername(userService);
        System.out.print("Enter name: ");
        String name = SC.nextLine();
        System.out.print("Enter surname: ");
        String surname = SC.nextLine();
        Role role = getRoleForNewUser();
        System.out.print("Enter password: ");
        String password = SC.nextLine();

        userService.addUser(new User(username, name, surname, password, role));
        System.out.println("User created successfully");
    }

    private static Role getRoleForNewUser() {

        System.out.println("Select user role");

        while (true) {
            System.out.println("[1] Simple");
            System.out.println("[2] Admin");
            String choice = SC.nextLine();

            switch (choice) {
                case "1":
                    return SIMPLE;
                case "2":
                    return ADMIN;
                default:
                    System.out.println(UNRECOGNIZED_INPUT);
            }
        }
    }

    private static void addNewBook(BookService bookService) throws IOException {

        System.out.println("------------ADD NEW BOOK------------");
        String name = getValidBookName(bookService);
        Genre genre = getGenreForNewBook();
        System.out.print("Enter number of books: ");
        int amount = SC.nextInt();
        SC.nextLine();

        bookService.addBook(new Book(name, genre));
        System.out.println("Book added successfully");
    }

    private static void deleteValidBook(BookService bookService) throws BookException, IOException {

        System.out.println("--------BOOK DELETION-------------");
        System.out.print("Enter book name: ");
        String bookName = SC.nextLine();

        bookService.deleteBook(bookName);
    }

    private static Genre getGenreForNewBook() {

        System.out.println("Select book genre");

        while (true) {
            System.out.println("[1] Fantasy");
            System.out.println("[2] Classics");
            System.out.println("[3] Detective");
            System.out.println("[4] Horror");
            System.out.println("[5] Action");

            String choice = SC.nextLine();

            switch (choice) {
                case "1":
                    return FANTASY;
                case "2":
                    return CLASSICS;
                case "3":
                    return DETECTIVE;
                case "4":
                    return HORROR;
                case "5":
                    return ACTION;
                default:
                    System.out.println(UNRECOGNIZED_INPUT);
            }
        }
    }

    private static void printGenreMenu() {
        System.out.println("[1] Fantasy");
        System.out.println("[2] Classics");
        System.out.println("[3] Detective");
        System.out.println("[4] Horror");
        System.out.println("[5] Action");
        System.out.println("[6] Exit");
    }

}





