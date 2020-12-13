package com.computer.shop.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.computer.shop.database.Database;
import com.computer.shop.models.User;
import com.computer.shop.settings.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.computer.shop.controllers.Authentication.toMd5;

public class Users {

    public static String postUser(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        String email = request.get("email");
        if (firstName == null) {
            data.put("status", "4");
        } else {
            try {
                if (!firstName.isEmpty()) {
                    String password = generateRandomPassword(12);
                    String sql = "INSERT INTO Users (FirstName, LastName, Email, Type, Password, Verified) VALUES (?,?,?,?,?,?)";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setString(3, email);
                    preparedStatement.setString(4, "client");
                    preparedStatement.setString(5, toMd5(password));
                    preparedStatement.setInt(6, 1);
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        sendMail(email, "Hello !\nYour account has been created successfully !\nThis is your password :\n" + password + "\nSincerely !");
                        data.put("status", "1");
                    } else {
                        data.put("status", "2");
                    }
                } else {
                    data.put("status", "3");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String updateUser(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String id = req.params(":id");
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        String email = request.get("email");
        if (firstName == null) {
            data.put("status", "4");
        } else {
            try {
                if (!firstName.isEmpty()) {
                    String sql = "UPDATE Users SET FirstName = ?, LastName = ?, Email = ? WHERE Id = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setString(3, email);
                    preparedStatement.setInt(4, Integer.parseInt(id));
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        data.put("status", "1");
                    } else {
                        data.put("status", "2");
                    }
                } else {
                    data.put("status", "3");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String getUsers(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> data = new HashMap<>();
        String token = req.headers("Authorization");
        if (token == null) {
            res.status(404);
            data.put("status", "error: No token");
        } else {
            try {
                if (!token.isEmpty()) {
                    String sql = "SELECT * FROM Users WHERE Type = 'client'";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<User> users = new ArrayList<>();
                    while (resultSet.next()) {
                        User user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), "");
                        users.add(user);
                    }
                    return gson.toJson(users);
                } else {
                    res.status(404);
                    data.put("status", "token is empty");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String getUser(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> data = new HashMap<>();
        String token = req.headers("Authorization");
        String email = null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            Claim claim = jwt.getHeaderClaim("email");
            email = claim.asString();
        } catch (JWTDecodeException exception) {
            //Invalid token
        }
        if (token == null) {
            data.put("status", "error: No token");
        } else {
            try {
                if (!token.isEmpty()) {
                    String sql = "SELECT Id, FirstName, LastName, Email, Type FROM Users WHERE email = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    User user = null;
                    while (resultSet.next()) {
                        user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), "");
                    }
                    return gson.toJson(user);
                } else {
                    data.put("status", "token is empty");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String getUserById(Request req, Response res) {
        res.type("application/json");
        String id = req.params(":id");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String token = req.headers("Authorization");
        if (token == null) {
            data.put("status", "error: No token");
        } else {
            try {
                if (!token.isEmpty()) {
                    String sql = "SELECT Id, FirstName, LastName, Email, Type FROM Users WHERE Id = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    User user = null;
                    while (resultSet.next()) {
                        user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), "");
                    }
                    return gson.toJson(user);
                } else {
                    data.put("status", "token is empty");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String verifyUserEmail(Request req, Response res) {
        res.type("application/json");
        String token = req.params(":token");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        if (token == null) {
            data.put("status", "error: No token");
        } else {
            try {
                if (!token.isEmpty()) {
                    String email = null;
                    try {
                        DecodedJWT jwt = JWT.decode(token);
                        Claim claim = jwt.getClaim("email");
                        email = claim.asString();
                    } catch (JWTDecodeException exception) {
                        data.put("message", "Invalid token !");
                        return gson.toJson(data);
                    }
                    String sql = "UPDATE Users SET Verified = 1 WHERE Email = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, email);
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        data.put("message", "Your email has been verified successfully !");
                    } else {
                        data.put("status", "2");
                    }
                    return gson.toJson(data);
                } else {
                    data.put("status", "token is empty");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String deleteUser(Request req, Response res) {
        String id = req.params(":id");
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> data = new HashMap<>();
        if (id == null) {
            data.put("status", "error: No Data!");
        } else {
            try {
                if (!id.isEmpty()) {
                    String sql = "DELETE FROM Users WHERE Id = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        data.put("status", "1");
                    } else {
                        data.put("status", "2");
                    }
                } else {
                    data.put("status", "3");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }


    private static String generateRandomPassword(int maxLength) {
        String upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String specialChars = "!@#$%^&*[]()_-+=<>?/{}~|";
        String allowedChars = "";

        Random random = new Random();
        StringBuilder sb = new StringBuilder(maxLength);

        allowedChars += upperCaseChars;
        sb.append(upperCaseChars.charAt(random.nextInt(upperCaseChars.length() - 1)));
        allowedChars += lowerCaseChars;
        sb.append(lowerCaseChars.charAt(random.nextInt(lowerCaseChars.length() - 1)));
        allowedChars += numberChars;
        sb.append(numberChars.charAt(random.nextInt(numberChars.length() - 1)));
        allowedChars += specialChars;
        sb.append(specialChars.charAt(random.nextInt(specialChars.length() - 1)));

        for (int i = sb.length(); i < maxLength; i += 1) {
            sb.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return sb.toString();
    }


    public static void sendMail(String to, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        Session mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Settings.email, Settings.emailPassword);
                    }
                });

        try {
            MimeMessage mm = new MimeMessage(mSession);
            mm.setFrom(new InternetAddress(Settings.email));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mm.setSubject("Computer Shop");
            mm.setText(content);
            Transport.send(mm);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
