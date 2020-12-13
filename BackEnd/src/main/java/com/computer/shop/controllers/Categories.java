package com.computer.shop.controllers;

import com.computer.shop.database.Database;
import com.computer.shop.models.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;

public class Categories {

    public static String getCategories(Request req, Response res) {
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
                    String sql = "SELECT * FROM Categories";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Category> categories = new ArrayList<>();
                    while (resultSet.next()) {
                        Category category = new Category(resultSet.getInt(1), resultSet.getString(2));
                        categories.add(category);
                    }
                    return gson.toJson(categories);
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

    public static String getCategory(Request req, Response res) {
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
                    String sql = "SELECT * FROM Categories WHERE Id = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Category> categories = new ArrayList<>();
                    while (resultSet.next()) {
                        Category category = new Category(resultSet.getInt(1), resultSet.getString(2));
                        categories.add(category);
                    }
                    return gson.toJson(categories);
                } else {
                    data.put("status", "token is empty");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(data);
    }

    public static String deleteCategory(Request req, Response res) {
        String id = req.params(":id");
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> data = new HashMap<>();
        if (id == null) {
            data.put("status", "error: No Data!");
        } else {
            try {
                if (!id.isEmpty()) {
                    String sql = "DELETE FROM Categories WHERE Id = ?";
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

    public static String postCategory(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String name = request.get("name");
        if (name == null) {
            data.put("status", "4");
        } else {
            try {
                if (!name.isEmpty()) {
                    String sql = "INSERT INTO Categories (Name) VALUES (?)";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setString(1, name);
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
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    data.put("status", "5");
                    return gson.toJson(data);
                }
            }
        }
        return gson.toJson(data);
    }
}
