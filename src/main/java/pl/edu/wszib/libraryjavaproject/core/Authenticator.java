package pl.edu.wszib.libraryjavaproject.core;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.libraryjavaproject.database.UserDAO;
import pl.edu.wszib.libraryjavaproject.model.User;

import java.util.Optional;

public class Authenticator {
    final UserDAO userDAO = UserDAO.getInstance();

    private static final Authenticator instance = new Authenticator();
    private Optional<User> loggedUser = Optional.empty();

    private final String seed = "Dd!VOq7Y7jJzFhWg7YhYJsk4Kp8X9@dGIt*WXAPH";

    private Authenticator(){
    }

    public void authenticate(User user){
        Optional<User> userFromDB = this.userDAO.findByLogin(user.getLogin());
        if(userFromDB.isPresent() && userFromDB.get().getPassword().equals(
                        DigestUtils.md5Hex(user.getPassword() + this.seed))){
            this.loggedUser = userFromDB;
        }
    }

    public void setLoggedUserToEmpty(){
        this.loggedUser = Optional.empty();
    }

    public Optional<User> getLoggedUser() {
            return loggedUser;
    }

    public String getSeed() {
        return seed;
    }

    public static Authenticator getInstance() {
        return instance;
    }
}
