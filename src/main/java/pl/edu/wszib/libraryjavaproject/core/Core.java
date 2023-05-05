package pl.edu.wszib.libraryjavaproject.core;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.libraryjavaproject.database.BookDAO;
import pl.edu.wszib.libraryjavaproject.database.UserDAO;
import pl.edu.wszib.libraryjavaproject.gui.GUI;
import pl.edu.wszib.libraryjavaproject.model.Book;
import pl.edu.wszib.libraryjavaproject.model.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

public class Core {
    final Authenticator authenticator = Authenticator.getInstance();
    final GUI gui = GUI.getInstance();
    final UserDAO userDAO = UserDAO.getInstance();
    final BookDAO bookDAO = BookDAO.getInstance();

    private static final Core instance = new Core();
    private Core() {
    }

    public void start(){
        boolean isRunning = false;
        int counter = 0;
        while (!isRunning && counter < 3){
            switch (this.gui.showLoggingMenu()) {
                case "1" -> {
                    this.authenticator.authenticate(this.gui.readLoginAndPassword());
                    isRunning = this.authenticator.getLoggedUser().isPresent();
                    counter++;
                    if (!isRunning) {
                        System.out.println("Not authorized!");
                    }
                }
                case "2" -> {
                    User user = this.gui.readLoginAndPassword();
                    this.gui.setUserName(user);
                    if (this.userDAO.findByLogin(user.getLogin()).isEmpty()) {
                        this.userDAO.userAdd(user.getLogin(),
                                DigestUtils.md5Hex(user.getPassword() + this.authenticator.getSeed()),
                                user.getName(),
                                user.getSurname());
                    } else {
                        System.out.println("Login is taken!");
                    }
                }
                case "3" -> System.exit(0);
                default -> System.out.println("Wrong choice!");
            }
        }
        while (isRunning){
            switch (this.gui.showMenu()){
                case "1":
                    String choice = this.gui.findBookByMenu();
                    String filtr = this.gui.getScanner().nextLine();
                    switch (choice){
                        case "1" -> this.bookDAO.getAllBooks().stream().filter(
                                    s -> s.getTitle().contains(filtr))
                                    .forEach(System.out::println);
                        case "2" -> this.bookDAO.getAllBooks().stream().filter(
                                        s -> s.getAuthor().contains(filtr))
                                .forEach(System.out::println);
                        case "3" -> this.bookDAO.getAllBooks().stream().filter(
                                        s -> Long.toString(s.getIsbn()).contains(filtr))
                                .forEach(System.out::println);
                        default -> System.out.println("Wrong choice! ");
                    }
                    break;
                case "2": //list all books, if logged user is admin than it prints renter data
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN) {
                        this.bookDAO.getAllBooks().stream().sorted(new Comparator<Book>() {
                            @Override
                            public int compare(Book o1, Book o2) {
                                return o1.getTitle().compareTo(o2.getTitle());
                            }
                        }).forEach(s -> {
                            if(s.isRent()){
                                System.out.println(s.toString().replace("']", (", renter: '[id] = " +
                                        s.getRenter().get().getId() + "' [name] = '" +
                                        s.getRenter().get().getName() + "' [surname] = '" +
                                        s.getRenter().get().getSurname() + "']")));
                            } else {
                                System.out.println(s.toString().replace("']", (", renter: '" +
                                        s.getRenter().orElse(new User("-")).getLogin() + "']")));
                            }
                        });
                    } else {
                        this.bookDAO.getAllBooks().forEach(System.out::println);
                    }
                    break;
                case "3": //logout
                    isRunning = false;
                    this.authenticator.setLoggedUserToEmpty();
                    instance.start();
                    break;
                case "4": //exit
                    isRunning = false;
                    break;
                case "5": //rent a book
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN) {
                        System.out.print("Renter login: ");
                        Optional<User> user = this.userDAO.findByLogin(this.gui.getScanner().nextLine());
                        if (user.isPresent()) {
                            System.out.print("Book ID: ");
                            if(this.bookDAO.rentBook(this.gui.getScanner().nextInt(),
                                    user.get().getLogin(), LocalDate.now())){
                                System.out.println("Rent succesful");
                            } else {
                                System.out.println("Something went wrong!");
                            }
                        } else {
                            System.out.println("User doesn't exist. ");
                        }
                        break;
                    }
                case "6":
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN){
                        System.out.print("ID of returned book: ");
                        bookDAO.returnBook(this.gui.getScanner().nextInt());
                        System.out.println("Book has been returned!");
                        break;
                    }
                case "7":
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN){
                        Book book = this.gui.readTitleAuthorAndIsbn();
                        bookDAO.addBook(book.getTitle(), book.getAuthor(), book.getIsbn());
                        break;
                    }
                case "8": //list all users
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN){
                        this.userDAO.getAllUsers().stream().forEach(s -> System.out.println(s.toString()));
                        break;
                    }
                case "9":
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN) {
                        this.bookDAO.getAllRentedBooks().stream().forEach(s ->
                                System.out.println(s.toString().replace("']", (", renter: '[id] = " +
                                        s.getRenter().get().getId() + "' [name] = '" +
                                        s.getRenter().get().getName() + "' [surname] = '" +
                                        s.getRenter().get().getSurname() + "']"))));
                        break;
                    }
                case "10":
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN){
                        this.bookDAO.getRentedBookWithExeceededReturnTime().forEach(s ->
                                System.out.println(s.toString().replace("']", (", renter: '[id] = " +
                                        s.getRenter().get().getId() + "' [name] = '" +
                                        s.getRenter().get().getName() + "' [surname] = '" +
                                        s.getRenter().get().getSurname() + "']"))));
                        break;
                    }
                default:
                    System.out.println("Wrong choice!");
                    break;
            }

        }
    }

    public static Core getInstance() {
        return instance;
    }
}
