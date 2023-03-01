package pl.edu.wszib.libraryjavaproject.model;

import java.time.LocalDate;
import java.util.Optional;

public class Book {
    private int id;
    private String title;
    private String author;
    private long isbn;
    private boolean isRent;
    private Optional<User> renter = Optional.empty();
    private LocalDate returnDate;

    public Book() {
    }

    public Book(int id, String title, String author, long isbn, boolean isRent) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isRent = isRent;
    }

    public Book(int id, String title, String author, long isbn, boolean isRent, Optional<User> renter) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isRent = isRent;
        this.renter = renter;
    }

    public Book(int id, String title, String author, long isbn,
                boolean isRent, Optional<User> renter, LocalDate returnDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isRent = isRent;
        this.renter = renter;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public boolean isRent() {
        return isRent;
    }

    public void setRent(boolean rent) {
        isRent = rent;
    }

    public Optional<User> getRenter() {
        return renter;
    }

    public void setRenter(Optional<User> renter) {
        this.renter = renter;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[Book][")
                .append("id: '")
                .append(id)
                .append("', title: '")
                .append(title)
                .append("', author: '")
                .append(author)
                .append("', isbn: '")
                .append(isbn)
                .append("',\n isRent: '")
                .append(isRent)
                .append("']");
        return sb.toString();
    }
}
