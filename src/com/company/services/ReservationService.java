package com.company.services;

import com.company.Book;
import com.company.Reservation;
import com.company.enums.Genre;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReservationService {

    private String path;

    public ReservationService(String path) {
        this.path = path;
    }
//    private String name;
//    private String readerUsername;
//    private boolean bookReservation;
//    private boolean confirmBookReservation;
//    private boolean returnBook;
//    private boolean confirmReturnBook;
//    private String librarianName;

    public ArrayList<Reservation> getAllReports() throws FileNotFoundException {

        File file = new File(path);
        Scanner sc = new Scanner(file);
        ArrayList<Reservation> reservations = new ArrayList<>();

        while (sc.hasNextLine()) {
            String name = sc.nextLine();
            String readerName = sc.nextLine();
            Boolean bookReservation = true;
            Boolean confirmBookReservation = false;
            Boolean returnBook = false;
            Boolean confirmReturnBook =false;
            String librarianName = sc.nextLine();
            sc.nextLine();

            reservations.add(new Reservation(name, readerName,bookReservation, confirmBookReservation, returnBook, confirmReturnBook, librarianName));
        }
        return reservations;
    }
}
