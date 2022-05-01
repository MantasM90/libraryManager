package com.company.services;

import com.company.BookStatus;
import com.company.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BookStatusService {

    private String path;

    public BookStatusService(String path) {
        this.path = path;
    }

    public ArrayList<BookStatus> getAllBookStatusEntries() throws FileNotFoundException {

        File file = new File(path);
        Scanner sc = new Scanner(file);
        ArrayList<BookStatus> bookStatuses = new ArrayList<>();

        while (sc.hasNextLine()) {
            String bookName = sc.nextLine();
            String readerName = sc.nextLine();
            Boolean bookReservation = sc.nextBoolean();
            Boolean confirmBookReservation = sc.nextBoolean();
            Boolean returnBook = sc.nextBoolean();
            Boolean confirmReturnBook = sc.nextBoolean();
            String librarianName = sc.nextLine();
            sc.nextLine();
            sc.nextLine();

            bookStatuses.add(new BookStatus(bookName, readerName,bookReservation,confirmBookReservation,returnBook,confirmReturnBook,librarianName));
        }
        return bookStatuses;
    }

    public ArrayList<BookStatus> getAllBookStatusEntriesByReaderUsername(User readerUsername) throws FileNotFoundException {

        ArrayList<BookStatus> statusEntries = getAllBookStatusEntries();
        ArrayList<BookStatus> statusEntriesByReaderName = new ArrayList<>();

        for (BookStatus statusEntry : statusEntries) {

            if(statusEntry.getReaderUsername().equals(readerUsername.getUsername())) {
                statusEntriesByReaderName.add(statusEntry);
            }
        }
        return statusEntriesByReaderName;
    }

    public void addBookStatusEntry(BookStatus bookStatusEntry) throws IOException {

        FileWriter fw = new FileWriter(path, true);
        PrintWriter writer = new PrintWriter(fw);

        writeBookStatusEntry(writer, bookStatusEntry);
        writer.close();
    }

    public void confirmTakenBook(User librarian, String bookName,String username) throws IOException {

        ArrayList<BookStatus> newAllBookStatusEntries = new ArrayList<>();

        for (BookStatus statusEntry: getAllBookStatusEntries()) {

            if(statusEntry.getBookName().equalsIgnoreCase(bookName) && statusEntry.getReaderUsername().equalsIgnoreCase(username) && statusEntry.isBookReservation()) {
                statusEntry.setBookReservation(false);
                statusEntry.setConfirmBookReservation(true);
                statusEntry.setLibrarianName(librarian.getUsername());
            }
            newAllBookStatusEntries.add(statusEntry);
        }
        rewriteAllBookStatusEntries(newAllBookStatusEntries);
    }

    public void returnBook(User reader, String bookName) throws IOException {

        ArrayList<BookStatus> newAllBookStatusEntries = new ArrayList<>();

        for (BookStatus statusEntry: getAllBookStatusEntries()) {

            if(statusEntry.getBookName().equalsIgnoreCase(bookName) && statusEntry.getReaderUsername().equalsIgnoreCase(reader.getUsername()) && statusEntry.isConfirmBookReservation()) {
                statusEntry.setReturnBook(true);
            }
            newAllBookStatusEntries.add(statusEntry);
        }
        rewriteAllBookStatusEntries(newAllBookStatusEntries);
    }

    public void confirmReturnBook(String bookName, String username) throws IOException {

        ArrayList<BookStatus> newAllBookStatusEntries = new ArrayList<>();

        for (BookStatus statusEntry: getAllBookStatusEntries()) {

            if(statusEntry.getBookName().equalsIgnoreCase(bookName) && statusEntry.getReaderUsername().equalsIgnoreCase(username) && statusEntry.isReturnBook()) {
                statusEntry.setReturnBook(false);
                statusEntry.setConfirmReturnBook(true);
            }
            newAllBookStatusEntries.add(statusEntry);
        }
        rewriteAllBookStatusEntries(newAllBookStatusEntries);
    }


    private void writeBookStatusEntry(PrintWriter writer, BookStatus bookStatusEntry) {
        writer.println(bookStatusEntry.getBookName());
        writer.println(bookStatusEntry.getReaderUsername());
        writer.println(bookStatusEntry.isBookReservation());
        writer.println(bookStatusEntry.isConfirmBookReservation());
        writer.println(bookStatusEntry.isReturnBook());
        writer.println(bookStatusEntry.isConfirmReturnBook());
        writer.println(bookStatusEntry.getLibrarianName());
        writer.println();

    }

    private void rewriteAllBookStatusEntries(ArrayList<BookStatus> bookStatusEntries) throws IOException {

        FileWriter fw = new FileWriter(path);
        PrintWriter writer = new PrintWriter(fw);

        for (BookStatus statusEntry: bookStatusEntries) {
            writeBookStatusEntry(writer, statusEntry);
        }

        writer.close();
    }


}
