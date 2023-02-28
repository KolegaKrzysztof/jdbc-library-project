package pl.edu.wszib.libraryjavaproject;

import org.apache.commons.codec.digest.DigestUtils;
import pl.edu.wszib.libraryjavaproject.core.Core;

public class App {
    public static void main(String[] args) {
//        System.out.println(DigestUtils.md5Hex("root" + "Dd!VOq7Y7jJzFhWg7YhYJsk4Kp8X9@dGIt*WXAPH"));
        Core.getInstance().start();
    }
}
