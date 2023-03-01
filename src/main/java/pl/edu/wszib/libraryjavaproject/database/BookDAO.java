package pl.edu.wszib.libraryjavaproject.database;

import pl.edu.wszib.libraryjavaproject.model.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO {
    private static final BookDAO instance = new BookDAO();
    final Connector connector = Connector.getInstance();
    final UserDAO userDAO = UserDAO.getInstance();

    private BookDAO(){
    }

    public boolean rentBook(int bookId, String login, LocalDate rentDate){
        try {
            String sql = "SELECT * FROM tbook WHERE id = ?";
            PreparedStatement ps = this.connector.getConnection().prepareStatement(sql);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                if(!rs.getBoolean("is_rented")) {
                    String updateSql = "UPDATE tbook SET is_rented = ? WHERE id = ?";
                    PreparedStatement updatePs = this.connector.getConnection().prepareStatement(updateSql);
                    updatePs.setBoolean(1, true);
                    updatePs.setInt(2, bookId);
                    updatePs.executeUpdate();
                    String sql3 = "INSERT INTO tbookrenter " +
                            "(renter_id, book_id, rent_date, return_date) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps3 = connector.getConnection().prepareStatement(sql3);
                    ps3.setInt(1, this.userDAO.findByLogin(login).get().getId());
                    ps3.setInt(2, bookId);
                    ps3.setDate(3, Date.valueOf(rentDate));
                    ps3.setDate(4, Date.valueOf(rentDate.plusDays(14)));
                    ps3.executeUpdate();
                    return true;
                } else {
                    System.out.println("Book is rented!");
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

    public void returnBook(int bookId){
        try {
            String sql = "DELETE FROM tbookrenter WHERE book_id = ?";
            PreparedStatement ps = connector.getConnection().prepareStatement(sql);
            ps.setInt(1, bookId);
            ps.executeUpdate();
            String updateSql = "UPDATE tbook SET is_rented = ? WHERE id = ?";
            PreparedStatement updatePs = this.connector.getConnection().prepareStatement(updateSql);
            updatePs.setBoolean(1, false);
            updatePs.setInt(2, bookId);
            updatePs.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tbook";
            PreparedStatement ps = this.connector.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Optional<String> renter = Optional.empty();
                if(rs.getBoolean("is_rented")){
                    renter = this.getRenterLoginByBookId(rs.getInt("id"));
                }
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getLong("isbn"),
                        rs.getBoolean("is_rented"),
                        this.userDAO.findByLogin(renter.orElse("-"))));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return books;
    }

    public List<Book> getAllRentedBooks(){
        List<Book> rentedBooks = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tbook WHERE is_rented = true";
            PreparedStatement ps = this.connector.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Optional<String> renter = Optional.empty();
                if(rs.getBoolean("is_rented")){
                    renter = this.getRenterLoginByBookId(rs.getInt("id"));
                }
                rentedBooks.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getLong("isbn"),
                        rs.getBoolean("is_rented"),
                        this.userDAO.findByLogin(renter.orElse("-"))));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return rentedBooks;
    }

    public List<Book> getRentedBookWithExeceededReturnTime(){
        List<Book> books = new ArrayList<>();
        try {
            String sql = "SELECT * FROM tbook JOIN tbookrenter ON tbook.id = tbookrenter.book_id " +
                    "WHERE tbook.is_rented = ? AND tbookrenter.return_date < ?";
            PreparedStatement ps = connector.getConnection().prepareStatement(sql);
            ps.setBoolean(1, true);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getLong("isbn"),
                        rs.getBoolean("is_rented"),
                        this.userDAO.findByLogin(this.getRenterLoginByBookId(
                                rs.getInt("id")).get())));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return books;
    }

    public Optional<String> getRenterLoginByBookId(int bookId){
        try {
            String sql = "SELECT tuser.login FROM tuser " +
                    "JOIN tbookrenter ON tuser.id = tbookrenter.renter_id " +
                    "WHERE tbookrenter.book_id = ?";
            PreparedStatement ps = connector.getConnection().prepareStatement(sql);
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return Optional.of(rs.getString("login"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public void addBook(String title, String author, long isbn){
        try {
            String sql = "INSERT INTO tbook (title, author, isbn) VALUES (?, ?, ?)";
            PreparedStatement ps = connector.getConnection().prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setLong(3, isbn);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static BookDAO getInstance() {
        return instance;
    }
}
