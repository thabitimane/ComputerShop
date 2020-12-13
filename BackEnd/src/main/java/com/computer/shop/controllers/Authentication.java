package com.computer.shop.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.computer.shop.database.Database;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Authentication {

    public static String login(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String email = request.get("email");
        String password = toMd5(request.get("password"));
        if (email == null || password == null) {
            data.put("status", "error: No Data!");
        } else {
            try {
                if (email.contains("@")) {
                    String sql = "SELECT Password,Type,Id,Verified from Users WHERE Email = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, email);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int verified = resultSet.getInt(4);
                        if (verified == 0) {
                            data.put("status", "5");
                            return gson.toJson(data);
                        }
                        String retrievedPassword = resultSet.getString(1);
                        String userType = resultSet.getString(2);
                        int userId = resultSet.getInt(3);
                        if (password.equals(retrievedPassword)) {
                            String token = "";
                            try {
                                Algorithm algorithm = Algorithm.HMAC256("J+hdXRoMCJ9@AbIJTDMFc7yU#a5MhvcP03}nJPyCPzZtQcGEp");
                                token = JWT.create()
                                        .withClaim("type", userType)
                                        .withClaim("email", email)
                                        .sign(algorithm);
                            } catch (JWTCreationException exception) {
                                //Invalid Signing configuration / Couldn't convert Claims.
                            }
                            data.put("status", "1");
                            data.put("token", token);
                            data.put("id", String.valueOf(userId));
                            data.put("type", userType);
                        } else {
                            data.put("status", "2");
                        }
                    } else {
                        data.put("status", "3");
                    }
                } else {
                    data.put("status", "4");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String register(Request req, Response res) {
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
        String password = request.get("password");
        if (email == null || password == null) {
            data.put("status", "error: No Data!");
        } else {
            try {
                if (email.contains("@")) {
                    String sql = "INSERT INTO Users (FirstName, LastName, Email, Password,Type) VALUES (?,?,?,?,?)";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, firstName);
                    preparedStatement.setString(2, lastName);
                    preparedStatement.setString(3, email);
                    preparedStatement.setString(4, toMd5(password));
                    preparedStatement.setString(5, "client");
                    int result = preparedStatement.executeUpdate();
                    int id;
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            id = (int) generatedKeys.getLong(1);
                        } else {
                            throw new SQLException("Creating user failed, no ID obtained.");
                        }
                    }
                    if (result == 1) {
                        String query = "INSERT INTO Carts (User) VALUES (?)";
                        preparedStatement = Database.connection.prepareStatement(query);
                        preparedStatement.setInt(1, id);
                        result = preparedStatement.executeUpdate();
                        if (result == 1) {
                            data.put("status", "1");
                            String token = "";
                            try {
                                Algorithm algorithm = Algorithm.HMAC256("J+hdXRoMCJ9@AbIJTDMFc7yU#a5MhvcP03}nJPyCPzZtQcGEp");
                                token = JWT.create()
                                        .withClaim("type", "client")
                                        .withClaim("email", email)
                                        .sign(algorithm);
                            } catch (JWTCreationException exception) {
                                //Invalid Signing configuration / Couldn't convert Claims.
                            }
                            String link = "http://localhost:8080/users/verify/" + token;
                            Users.sendMail(email, "Hello !\nYour account has been created successfully !\nPlease click here to verify your email :\n" + link + "\nSincerely !");

                        }
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

    public static String toMd5(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
