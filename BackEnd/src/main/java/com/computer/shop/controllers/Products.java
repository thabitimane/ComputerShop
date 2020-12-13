package com.computer.shop.controllers;

import com.computer.shop.database.Database;
import com.computer.shop.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

public class Products {

    public static String postProduct(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String name = request.get("name");
        String description = request.get("description");
        String photo = request.get("photo");
        String category = request.get("category");
        String quantity = request.get("quantity");
        String price = request.get("price");
        if (name == null) {
            data.put("status", "4");
        } else {
            try {
                if (!name.isEmpty()) {

                    String photoName = generateName();
                    byte[] decodedBytes = Base64.getDecoder().decode(photo.split("base64,")[1]);
                    File file = new File(new File("").getAbsolutePath() + "/files/products/" + photoName + ".png");
                    FileOutputStream fop = new FileOutputStream(file);
                    fop.write(decodedBytes);
                    fop.flush();
                    fop.close();

                    String sql = "INSERT INTO Products (Name, Description, Photo, Category, Quantity, Price) VALUES (?,?,?,?,?,?)";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, photoName + ".png");
                    preparedStatement.setInt(4, Integer.parseInt(category));
                    preparedStatement.setInt(5, Integer.parseInt(quantity));
                    preparedStatement.setFloat(6, Float.parseFloat(price));
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        data.put("status", "1");
                    } else {
                        data.put("status", "2");
                    }
                } else {
                    data.put("status", "3");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String getProducts(Request req, Response res) {
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
                    String sql = "SELECT * FROM Products";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Product> products = new ArrayList<>();
                    while (resultSet.next()) {
                        Product product = new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getFloat(7));
                        products.add(product);
                    }
                    return gson.toJson(products);
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

    public static String getProduct(Request req, Response res) {
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
                    String sql = "SELECT * FROM Products WHERE Id = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    Product product = null;
                    while (resultSet.next()) {
                        product = new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getFloat(7));
                    }
                    return gson.toJson(product);
                } else {
                    data.put("status", "token is empty");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String searchForProducts(Request req, Response res) {
        res.type("application/json");
        String query = req.params(":query");
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
                    String sql = "SELECT * FROM Products WHERE Name LIKE ? OR Description LIKE ? OR Category IN (SELECT Id From Categories WHERE Name LIKE ?)";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, query+"%");
                    preparedStatement.setString(2, query+"%");
                    preparedStatement.setString(3, query+"%");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Product> products = new ArrayList<>();
                    while (resultSet.next()) {
                        Product product = new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getFloat(7));
                        products.add(product);
                    }
                    return gson.toJson(products);
                } else {
                    data.put("status", "token is empty");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String deleteProduct(Request req, Response res) {
        String id = req.params(":id");
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> data = new HashMap<>();
        if (id == null) {
            data.put("status", "error: No Data!");
        } else {
            try {
                if (!id.isEmpty()) {
                    String sql = "DELETE FROM Products WHERE Id = ?";
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

    private static String generateName() {
        int maxLength = 10;
        String upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String allowedChars = "";

        Random random = new Random();
        StringBuilder sb = new StringBuilder(maxLength);

        allowedChars += upperCaseChars;
        sb.append(upperCaseChars.charAt(random.nextInt(upperCaseChars.length() - 1)));
        allowedChars += lowerCaseChars;
        sb.append(lowerCaseChars.charAt(random.nextInt(lowerCaseChars.length() - 1)));
        allowedChars += numberChars;
        sb.append(numberChars.charAt(random.nextInt(numberChars.length() - 1)));

        for (int i = sb.length(); i < maxLength; i += 1) {
            sb.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return sb.toString();
    }
}
