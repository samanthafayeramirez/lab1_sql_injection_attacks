package com.ias101.lab1.security;

import com.ias101.lab1.database.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Authentication class for user validation
 */
public class Authenticator {
    private static final String PASSWORD_PATTERN = "^(pass|password|userpass)\\d{1,3}$"; // Matches passwords like pass123, password456, userpass123
    ;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{5,20}$"; // Allows underscores in usernames

    /**
     * Authenticates a user by checking username and password against database
     *
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return boolean Returns true if authentication successful, false otherwise
     * @throws RuntimeException if there is a SQL error during authentication
     */
    public static boolean authenticateUser(String username, String password) {
      if (isValid(username, USERNAME_PATTERN)) {
          System.out.println(("Invalid username format."));
          return false;
          
      }
      if (isValid(password, PASSWORD_PATTERN)) {
          System.out.println("Invalid password format.");
          return false;
      }
      
        try(var conn = DBUtil.connect("jdbc:sqlite:src/main/resources/database/sample.db",
                "root","root")) {
            try(var statement = conn.createStatement()) {
                var query = """
                        SELECT * FROM user_data
                        WHERE username =\s""" + "'" + username + "'"
                        + "AND password = " + "'" + password + "'";
                System.out.println(query);
                ResultSet rs = statement.executeQuery(query);

                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static boolean isValid(String input, String pattern){
        Pattern pat= Pattern.compile(pattern);
        Matcher match= pat.matcher(input);
        return match.matches();

    }
}