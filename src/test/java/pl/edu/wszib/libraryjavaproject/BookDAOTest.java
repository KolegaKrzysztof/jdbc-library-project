package pl.edu.wszib.libraryjavaproject;

import org.junit.jupiter.api.*;
import pl.edu.wszib.libraryjavaproject.database.BookDAO;
import pl.edu.wszib.libraryjavaproject.database.Connector;
import pl.edu.wszib.libraryjavaproject.model.Book;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class BookDAOTest {


    @Test
    public void addBookTest(){
        final BookDAO bookDAO = BookDAO.getInstance();

        Book book = new Book(0, "Test Book", "Test Author",
                123L, false);

        bookDAO.addBook(book.getTitle(), book.getAuthor(), book.getIsbn());

        List<Book> allBooks = bookDAO.getAllBooks();
        Book lastBook = allBooks.get(allBooks.size()-1);
        Assertions.assertEquals(lastBook.getTitle(), book.getTitle(), "Added book title does not match");
        Assertions.assertEquals(lastBook.getAuthor(), book.getAuthor(), "Added book author does not match");
        Assertions.assertEquals(lastBook.getIsbn(), book.getIsbn(), "Added book ISBN does not match");
        Assertions.assertEquals(lastBook.isRent(), book.isRent(), "Added book rental status does not match");

        String sql = "DELETE FROM tbook WHERE id = ?";
        try {
            PreparedStatement ps = Connector.getInstance().getConnection().prepareStatement(sql);
            ps.setInt(1, lastBook.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't delete book record from DB after test");
            e.printStackTrace();
        }
    }
}
