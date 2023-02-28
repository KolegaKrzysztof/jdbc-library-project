package pl.edu.wszib.libraryjavaproject.core;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.libraryjavaproject.database.BookDAO;
import pl.edu.wszib.libraryjavaproject.database.Connector;
import pl.edu.wszib.libraryjavaproject.database.UserDAO;
import pl.edu.wszib.libraryjavaproject.gui.GUI;
import pl.edu.wszib.libraryjavaproject.model.Book;
import pl.edu.wszib.libraryjavaproject.model.User;

import java.time.LocalDate;
import java.util.Optional;

public class Core {
    final Authenticator authenticator = Authenticator.getInstance();
    final GUI gui = GUI.getInstance();
    final UserDAO userDAO = UserDAO.getInstance();
    final BookDAO bookDAO = BookDAO.getInstance();
    final Connector connector = Connector.getInstance();

    private static final Core instance = new Core();
    private Core() {
    }

    public void start(){
        boolean isRunning = false;
        int counter = 0;
        while (!isRunning && counter < 3){
            switch (this.gui.showLoggingMenu()){
                case "1":
                    this.authenticator.authenticate(this.gui.readLoginAndPassword());
                    isRunning = this.authenticator.getLoggedUser().isPresent();
                    counter++;
                    if(!isRunning){
                        System.out.println("Not authorized!");
                    }
                    break;
                case "2":
                    User user = this.gui.readLoginAndPassword();
                    this.gui.setUserName(user);
                    if(this.userDAO.findByLogin(user.getLogin()).isEmpty()){
                        this.userDAO.userAdd(user.getLogin(),
                                DigestUtils.md5Hex(user.getPassword() + this.authenticator.getSeed()),
                                user.getName(),
                                user.getSurname());
                    } else {
                        System.out.println("Login is taken!");
                    }
                    break;
                case "3":
//                    connector.close();
                    System.exit(0);
                default:
                    System.out.println("Wrong choice!");
                    break;
            }
        }
        while (isRunning){
            switch (this.gui.showMenu()){
                case "1":
                    System.out.println(1);
                    break;
                case "2": //list all books
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN) {
                        this.bookDAO.getAllBooks().stream().forEach(s -> {
                            if(s.isRent()){
//                                System.out.println(s.toString());
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
//                    connector.close();
                    instance.start();
                    break;
                case "4": //exit
                    isRunning = false;
                    break;
                case "5": //rent a book
//                    int bookId, String login, String name, String surname, LocalDate rentDate
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN) {
                        System.out.print("Renter login: ");
                        Optional<User> user = this.userDAO.findByLogin(this.gui.getScanner().nextLine());
                        if (user.isPresent()) {
                            System.out.print("Book ID: ");
                            if(this.bookDAO.rentBook(this.gui.getScanner().nextInt(),
                                    user.get().getLogin(),
                                    user.get().getName(),
                                    user.get().getSurname(),
                                    this.gui.readDataToRentBook())){
                                System.out.println("Rent succesful");
                            } else {
                                System.out.println("Something went wrong!");
                            }
                        } else {
                            System.out.println("User doesn't exist. ");
                        }
                    }
                    break;
                case "6":
                    System.out.print("ID of returned book: ");
                    bookDAO.returnBook(this.gui.getScanner().nextInt());
                    System.out.println("Book has been returned!");
                    break;
                case "7":
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN){
                        Book book = this.gui.readTitleAuthorAndIsbn();
                        bookDAO.addBook(book.getTitle(), book.getAuthor(), book.getIsbn());
                    }
                    break;
                case "8": //list all users
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN){
                        this.userDAO.getAllUsers().stream().forEach(s -> System.out.println(s.toString()));
                    }
                    break;
                case "9":
                    if(this.authenticator.getLoggedUser().isPresent() &&
                            this.authenticator.getLoggedUser().get().getRole() == User.Role.ADMIN) {
                        this.bookDAO.getAllRentedBooks().stream().forEach(s ->
                                System.out.println(s.toString().replace("']", (", renter: '[id] = " +
                                        s.getRenter().get().getId() + "' [name] = '" +
                                        s.getRenter().get().getName() + "' [surname] = '" +
                                        s.getRenter().get().getSurname() + "']"))));
                    } else {
                        this.bookDAO.getAllBooks().forEach(System.out::println);
                    }
                    break;
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
