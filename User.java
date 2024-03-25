import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// User class implements the Serializable interface for object serialization
public class User implements Serializable {
    private String username; // Username of the user
    private String password; // Password of the user

    // Static list to keep track of previous usernames across all instances of the User class
    private static final ArrayList<String> previousUsernames = new ArrayList<>();

    // Constructor to initialize a User object with a username and password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter method to retrieve the username of the user
    public String getUsername() {
        return this.username;
    }

    // Setter method to update the username of the user
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter method to retrieve the password of the user
    public String getPassword() {
        return this.password;
    }

    // Setter method to update the password of the user
    public void setPassword(String password) {
        this.password = password;
    }

    // Override toString method to provide a string representation of the User object
    @Override
    public String toString() {
        return "User{username='" + this.username + "', password='" + this.password + "'}";
    }
}
