package com.company;

public  class BookStatus {

    private String bookName;
    private String readerUsername;
    private boolean bookReservation;
    private boolean confirmBookReservation;
    private boolean returnBook;
    private boolean confirmReturnBook;
    private String librarianName;

    public BookStatus(String bookName, String readerUsername, boolean bookReservation, boolean confirmBookReservation, boolean returnBook, boolean confirmReturnBook, String librarianName) {
        this.bookName = bookName;
        this.readerUsername = readerUsername;
        this.bookReservation = bookReservation;
        this.confirmBookReservation = confirmBookReservation;
        this.returnBook = returnBook;
        this.confirmReturnBook = confirmReturnBook;
        this.librarianName = librarianName;
    }
    public BookStatus(String bookName, String readerUsername) {
        this.bookName = bookName;
        this.readerUsername = readerUsername;
        this.bookReservation = true;
        this.confirmBookReservation = false;
        this.returnBook = false;
        this.confirmReturnBook = false;
        this.librarianName = null;
    }

    public String getBookName() {
        return bookName;
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

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setReaderUsername(String readerUsername) {
        this.readerUsername = readerUsername;
    }

    public void setBookReservation(boolean bookReservation) {
        this.bookReservation = bookReservation;
    }

    public void setConfirmBookReservation(boolean confirmBookReservation) {
        this.confirmBookReservation = confirmBookReservation;
    }

    public void setReturnBook(boolean returnBook) {
        this.returnBook = returnBook;
    }

    public void setConfirmReturnBook(boolean confirmReturnBook) {
        this.confirmReturnBook = confirmReturnBook;
    }

    public void setLibrarianName(String librarianName) {
        this.librarianName = librarianName;
    }
}
