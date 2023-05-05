package pl.edu.wszib.libraryjavaproject.gui;

import pl.edu.wszib.libraryjavaproject.core.Authenticator;
import pl.edu.wszib.libraryjavaproject.model.Book;
import pl.edu.wszib.libraryjavaproject.model.User;

import java.time.LocalDate;
import java.util.Scanner;

public class GUI {

    private static final GUI instance = new GUI();
    final Authenticator authenticator = Authenticator.getInstance();

    private final Scanner scanner = new Scanner(System.in);

    private GUI(){
    }

    public String showLoggingMenu(){
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        return this.scanner.nextLine();
    }

    public String findBookByMenu(){
        System.out.println("""
                How do you want search for book?
                1. Title
                2. Author
                3. ISBN""");
        return this.scanner.nextLine();
    }

    public String showMenu(){
        System.out.print("""
                Menu:
                1. Find book
                2. List all books
                3. Logout
                4. Exit
                """);
        if(authenticator.getLoggedUser().isPresent() &&
                this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN){
            System.out.println("""
                    5. Rent a book.
                    6. Return book.
                    7. Add book
                    8. List users
                    9. List rented books
                    10. List books with exceeded return time""");
        }
        return this.scanner.nextLine();
    }

    public Book readTitleAuthorAndIsbn(){
        Book book = new Book();
        System.out.print("Title: ");
        book.setTitle(this.scanner.nextLine());
        System.out.print("Author: ");
        book.setAuthor(this.scanner.nextLine());
        System.out.print("isbn: ");
        book.setIsbn(this.scanner.nextLong());
        return book;
    }

    public void setUserName(User user){
        System.out.print("name: ");
        user.setName(this.scanner.nextLine());
        System.out.print("surname: ");
        user.setSurname(this.scanner.nextLine());
    }

    public User readLoginAndPassword(){
        User user = new User();
        System.out.println("Login: ");
        user.setLogin(this.scanner.nextLine());
        System.out.println("Passord: ");
        user.setPassword(this.scanner.nextLine());
        return user;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public static GUI getInstance() {
        return instance;
    }
}
