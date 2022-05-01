package com.company;

import com.company.enums.Genre;

import java.util.Objects;

public class Book {

    private String name;
    private Genre genre;

    public Book(String name, Genre genre) {
        this.name = name;
        this.genre = genre;

    }

    public String getName() {
        return name;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return name.equals(book.name) && genre == book.genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genre);
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", genre=" + genre +
                '}';
    }
}
