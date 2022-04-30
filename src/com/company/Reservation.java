package com.company;

public  class Reservation {

    private String name;
    private String readerUsername;
    private boolean bookReservation;
    private boolean confirmBookReservation;
    private boolean returnBook;
    private boolean confirmReturnBook;
    private String librarianName;

    public Reservation(String name, String readerUsername, boolean bookReservation, boolean confirmBookReservation, boolean returnBook, boolean confirmReturnBook, String librarianName) {
        this.name = name;
        this.readerUsername = readerUsername;
        this.bookReservation = bookReservation;
        this.confirmBookReservation = confirmBookReservation;
        this.returnBook = returnBook;
        this.confirmReturnBook = confirmReturnBook;
        this.librarianName = librarianName;
    }

    public String getName() {
        return name;
    }

    public String getReaderUsername() {
        return readerUsername;
    }

    public boolean isBookReservation() {
        return bookReservation;
    }

    public boolean isConfirmBookReservation() {
        return confirmBookReservation;
    }

    public boolean isReturnBook() {
        return returnBook;
    }

    public boolean isConfirmReturnBook() {
        return confirmReturnBook;
    }

    public String getLibrarianName() {
        return librarianName;
    }
}
