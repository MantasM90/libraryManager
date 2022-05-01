package com.company;

import com.company.enums.Genre;
import com.company.enums.Role;
import com.company.exeptions.AdminDeletionException;
import com.company.exeptions.BookException;
import com.company.exeptions.HasBookException;
import com.company.exeptions.UserException;
import com.company.services.BookService;
import com.company.services.BookStatusService;
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

    static UserService userService = new UserService(usersPath);
    static BookService bookService = new BookService(libraryPath);
    static BookStatusService bookStatusService = new BookStatusService(reservationReportsPath);

    public static void main(String[] args) {



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
                            userMenu(loggedInUser);
                        }  catch (UserException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "2":

                        registerSimpleNewUser();
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

    private static void userMenu(User user) throws IOException {

        if (user.getRole().equals(SIMPLE)) {

            simpleUserMenu(user);
        } else if (user.getRole().equals(ADMIN)) {

            adminUserMenu(user);
        }
    }

    private static void printMenu(){
        System.out.println("[1] Log in");
        System.out.println("[2] Register user (only reader)");
        System.out.println("[3] Exit");
    }

    private static void simpleUserMenu(User simpleUser) throws IOException {

        while (true) {
            printSimpleMenu();

            String choice = SC.nextLine();

            switch (choice) {

                case "1":
                    printSimpleUserDetails(simpleUser);
                    break;
                case "2":
                    printAllBooks(bookService.getAllBooks());
                    break;

                case "3":
                    Genre genre = getGenreForNewBook();
                    printAllBooks(bookService.getAllBooksByGenre(genre));
                    break;
                case "4":
                    orderBook(simpleUser);
                    break;
                case "5":
                    printAllBookStatusEntries(bookStatusService.getAllBookStatusEntriesByReaderUsername(simpleUser));
                    break;
                case "6":
                    printAllConfirmedBookStatusEntries(bookStatusService.getAllBookStatusEntriesByReaderUsername(simpleUser));
                    break;
                case "7":
                    returnBookToLibrarian(simpleUser);
                    break;
                case "8":
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

        System.out.println("[4] Order a book");
        System.out.println("[5] Show reserved books (pending librarian approval)");
        System.out.println("[6] Show taken books");
        System.out.println("[7] Return the book");
//        System.out.println("[7] Finished books");
        System.out.println("[8] Exit");
        System.out.println();
    }


    private static void adminUserMenu(User adminUser) throws IOException {

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
                    confirmOrderedBook(adminUser);
                    break;
                case "6":
                    bookReturnConfirmedByLibrarian();
                    break;
                case "7":
                    printAllBooksReservations(bookStatusService.getAllBookStatusEntries());
                    break;
                case "8":
                    printAllBooksReturnRequests(bookStatusService.getAllBookStatusEntries());
                    break;
                case "9":
                    printAllBooks(bookService.getAllBooks());
                    break;
                case "10":
                    Genre genre = getGenreForNewBook();
                    printAllBooks(bookService.getAllBooksByGenre(genre));
                    break;
                case "11":
                    addNewBook();
                    break;
                case "12":

                     try {
                         deleteValidBook();
                     } catch (BookException | IOException e) {
                         System.out.println(e.getMessage());
                     }
                    break;
                case "13":
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

        System.out.println("[5] Confirm book orders");
        System.out.println("[6] Confirm request to return a book");
        System.out.println("[7] Show all book reservations");
        System.out.println("[8] Show all requests to return a book");

        System.out.println("[9] Books list");
        System.out.println("[10] Books list by genre");
        System.out.println("[11] Add book");
        System.out.println("[12] Remove book");
        System.out.println("[13] Exit");
        System.out.println();
    }

    private static String getValidUsername() throws FileNotFoundException {

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

    private static String getValidBookName() throws FileNotFoundException {

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

    private static void printAllBooksReservations(ArrayList<BookStatus> bookStatuses) {

        System.out.println("All BOOK RESERVATIONS:");
        if (bookStatuses.size() > 0 ) {
            for (BookStatus bookStatus : bookStatuses) {
                if(bookStatus.isBookReservation())   {

                    System.out.println("---------------------------");
                    System.out.printf("Book name: %s\n",bookStatus.getBookName());
                    System.out.printf("Reader username: %s \n",bookStatus.getReaderUsername());
                    System.out.println("Reservation status: RESERVED");
                }
            }
            System.out.println();

        } else {
            System.out.println("Here is no reserved books");
        }

    }

    private static void printAllBooksReturnRequests(ArrayList<BookStatus> bookStatuses) {

        System.out.println("All REQUESTS TO RETURN A BOOK:");
        if (bookStatuses.size() > 0 ) {
            for (BookStatus bookStatus : bookStatuses) {
                if(bookStatus.isReturnBook())   {

                    System.out.println("---------------------------");
                    System.out.printf("Book name: %s \n",bookStatus.getBookName());
                    System.out.printf("Reader username: %s \n",bookStatus.getReaderUsername());
                    if(bookStatus.isReturnBook())
                        System.out.println("Book return status: RETURN REQUESTED");

                }
            }
            System.out.println();

        } else {
            System.out.println("Here is no book return requests");
        }
    }

    private static void printAllBookStatusEntries(ArrayList<BookStatus> bookStatuses) {

        System.out.println("Reserved books List:");
        if (bookStatuses.size() > 0 ) {
            for (BookStatus bookStatus : bookStatuses) {
                if(!bookStatus.isConfirmBookReservation())   {

                    System.out.println("---------------------------");
                    System.out.printf("Book by name %s was reserved.",bookStatus.getBookName());
                }
            }
            System.out.println();

        } else {
            System.out.println("Here is no reserved books");
        }
    }

    private static void printAllConfirmedBookStatusEntries(ArrayList<BookStatus> bookStatuses) {

        System.out.println("Taken books List:");
        if (bookStatuses.size() > 0 ) {
            for (BookStatus bookStatus : bookStatuses) {

                if(bookStatus.isConfirmBookReservation()) {

                    System.out.println("---------------------------");
                    System.out.printf("Book by name %s was taken. ", bookStatus.getBookName());
                    System.out.printf("Approved by librarian - %s", bookStatus.getLibrarianName());
                    // TODO: bug - getLibrarianName() not returning name of librarian.
                }
            }
            System.out.println();
        } else {
            System.out.println("Here is no taken books");
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

    private static void registerSimpleNewUser() throws IOException {

        System.out.println("------------NEW READER REGISTRATION------------");
        String username = getValidUsername();
        System.out.print("Enter name: ");
        String name = SC.nextLine();
        System.out.print("Enter surname: ");
        String surname = SC.nextLine();
        System.out.print("Enter password: ");
        String password = SC.nextLine();

        User user = new User(username, name, surname, password);
        userService.addUser(user);
        System.out.println("User created successfully");
        simpleUserMenu(user);
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
        String username = getValidUsername();
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

    // reader order book from library
    private static void orderBook(User simpleUser) throws IOException {

        System.out.println("RESERVE A BOOK");

        System.out.println("Enter book name:");
        String validBookName = SC.nextLine();
        String bookName = "";
        if (bookService.bookExists(validBookName)) {
            bookName = validBookName;
        }

        BookStatus bookStatusEntry = new BookStatus(bookName, simpleUser.getUsername());
        bookStatusService.addBookStatusEntry(bookStatusEntry);
        System.out.printf("Book '%s' successfuly reserved", bookName);
    }

    // librarian confirm book order from library
    private static void confirmOrderedBook(User adminUser) throws IOException {

        System.out.println("Enter book name:");
        String validBookName = SC.nextLine();
        String bookName = "";
        if (bookService.bookExists(validBookName)) {
            bookName = validBookName;
        }
        System.out.println("Enter reader name:");
        String username = SC.nextLine();

        bookStatusService.confirmTakenBook(adminUser,bookName,username);
        System.out.printf("User '%s' took a book '%s' from library.\n",username, bookName);
    }

    // reader send for confirmation
    private static void returnBookToLibrarian(User simpleUser) throws IOException {

        System.out.println("REQUEST TO RETURN BOOK TO LIBRARY");
        System.out.println("Enter book name:");
        String validBookName = SC.nextLine();
        String bookName = "";
        if (bookService.bookExists(validBookName)) {
            bookName = validBookName;
        }

        bookStatusService.returnBook(simpleUser,bookName);
        System.out.printf("Book '%s' successfuly set to confirm return list", bookName);
    }

    // librarian confirmed return of book
    private static void  bookReturnConfirmedByLibrarian() throws IOException {

        System.out.println("Enter book name:");
        String validBookName = SC.nextLine();
        String bookName = "";
        if (bookService.bookExists(validBookName)) {
            bookName = validBookName;
        }
        System.out.println("Enter reader name:");
        String username = SC.nextLine();

        bookStatusService.confirmReturnBook(bookName,username);
        System.out.printf("Book '%s' successfuly returned", bookName);
    }

    private static void addNewBook() throws IOException {

        System.out.println("------------ADD NEW BOOK------------");
        String name = getValidBookName();
        Genre genre = getGenreForNewBook();

        bookService.addBook(new Book(name, genre));
        System.out.println("Book added successfully");
    }

    private static void deleteValidBook() throws BookException, IOException {

        System.out.println("--------BOOK DELETION-------------");
        System.out.print("Enter book name: ");
        String bookName = SC.nextLine();

        bookService.deleteBook(bookName);
    }

    private static Genre getGenreForNewBook() {

        System.out.println("Select book genre");

        while (true) {

            printGenreMenu();
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





