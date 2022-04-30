package com.company.services;
import com.company.Book;
import com.company.enums.Genre;
import com.company.exeptions.BookException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class BookService {

    private String path;

    public BookService(String path) {
        this.path = path;
    }

    public ArrayList<Book> getAllBooks() throws FileNotFoundException {

        File file = new File(path);
        Scanner sc = new Scanner(file);
        ArrayList<Book> books = new ArrayList<>();

        while (sc.hasNextLine()) {
            String name = sc.nextLine();
            String genre = sc.nextLine();
            sc.nextLine();

            books.add(new Book(name,  Genre.valueOf(genre)));
        }
        return books;
    }


    public ArrayList<Book> getAllBooksByGenre(Genre genre) throws FileNotFoundException {

        ArrayList<Book> books = getAllBooks();
        ArrayList<Book> booksByGenre = new ArrayList<>();

        for (Book book : books) {

            if(book.getGenre().equals(genre)) {
                booksByGenre.add(book);
            }
        }
        return booksByGenre;
    }

    public void addBook(Book book) throws IOException {

        FileWriter fw = new FileWriter(path, true);
        PrintWriter writer = new PrintWriter(fw);

        writeBook(writer, book);

        writer.close();
    }

    public void deleteBook(String name) throws IOException, BookException {

        ArrayList<Book> newAllBooks = getAllBooks();
        for (Book book: getAllBooks()) {
            if (book.equals(getBookByName(name))) {
                newAllBooks.remove(getBookByName(name));
            }
        }
        rewriteAllBooks(newAllBooks);
    }


    public Book getBookByName(String name) throws BookException, FileNotFoundException {

        for (Book book: getAllBooks()) {
            if (book.getName().equalsIgnoreCase(name)) {
                return book;
            }
        }
        throw new BookException(String.format("Book with name '%s' not exist", name));
    }

    public boolean bookExists(String name) throws FileNotFoundException {

        ArrayList<Book> books = getAllBooks();

        for (Book book : books)
            if (book.getName().equalsIgnoreCase(name)) {
                return true;
            }
        return false;
    }

    private void rewriteAllBooks(ArrayList<Book> books) throws IOException {

        System.out.println("perrasyta");
        FileWriter fw = new FileWriter(path);
        PrintWriter writer = new PrintWriter(fw);

        for (Book book : books) {
            writeBook(writer, book);
        }

        writer.close();
    }

    private void writeBook(PrintWriter writer, Book book) {
        writer.println(book.getName());
        writer.println(book.getGenre());
        writer.println();
    }
}
