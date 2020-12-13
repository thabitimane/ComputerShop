package com.computer.shop.controllers;

import com.computer.shop.database.Database;
import com.computer.shop.models.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Carts {

    public static String getCartItems(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String id = req.params(":id");
        HashMap<String, String> data = new HashMap<>();
        String token = req.headers("Authorization");
        if (token == null) {
            res.status(404);
            data.put("status", "error: No token");
        } else {
            try {
                if (!token.isEmpty()) {
                    String sql = "SELECT Id FROM Carts WHERE User = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int cartId = resultSet.getInt(1);
                    sql = "SELECT * FROM CartItems WHERE Cart = ?";
                    preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, cartId);
                    resultSet = preparedStatement.executeQuery();
                    ArrayList<CartItem> cartItems = new ArrayList<>();
                    while (resultSet.next()) {
                        CartItem cartItem = new CartItem(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4));
                        cartItems.add(cartItem);
                    }
                    return gson.toJson(cartItems);
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

    public static String deleteCartItem(Request req, Response res) {
        String id = req.params(":id");
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> data = new HashMap<>();
        if (id == null) {
            data.put("status", "error: No Data!");
        } else {
            try {
                if (!id.isEmpty()) {
                    String sql = "DELETE FROM CartItems WHERE Id = ?";
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

    public static String postCartItem(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String item = request.get("item");
        String quantity = request.get("quantity");
        String id = request.get("id");
        if (item == null) {
            data.put("status", "4");
        } else {
            try {
                if (!item.isEmpty()) {
                    String sql = "SELECT Id FROM Carts WHERE User = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int cartId = resultSet.getInt(1);
                    String query = "INSERT INTO CartItems (Item, Quantity, Cart) VALUES (?,?,?)";
                    preparedStatement = Database.connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(item));
                    preparedStatement.setInt(2, Integer.parseInt(quantity));
                    preparedStatement.setInt(3, cartId);
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

    public static String checkout(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        String id = request.get("id");
        String address = request.get("address");
        if (id == null) {
            data.put("status", "4");
        } else {
            try {
                if (!id.isEmpty()) {
                    String sql = "SELECT Id FROM Carts WHERE User = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int cartId = resultSet.getInt(1);
                    sql = "SELECT * FROM CartItems WHERE Cart = ?";
                    preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, cartId);
                    resultSet = preparedStatement.executeQuery();
                    String query = "INSERT INTO Commands (User, Date, Time, Address,Status) VALUES (?,?,?,?,?)";
                    preparedStatement = Database.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    preparedStatement.setString(2, date);
                    preparedStatement.setString(3, time);
                    preparedStatement.setString(4, address);
                    preparedStatement.setString(5, "pending");
                    int result = preparedStatement.executeUpdate();
                    int commandId;
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            commandId = (int) generatedKeys.getLong(1);
                        } else {
                            throw new SQLException("Creating user failed, no ID obtained.");
                        }
                    }
                    if (result == 1) {
                        while (resultSet.next()) {
                            CartItem cartItem = new CartItem(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4));
                            query = "INSERT INTO CommandItems (Item, Quantity, Command) VALUES (?,?,?)";
                            preparedStatement = Database.connection.prepareStatement(query);
                            preparedStatement.setInt(1, cartItem.getItem());
                            preparedStatement.setInt(2, cartItem.getQuantity());
                            preparedStatement.setInt(3, commandId);
                            result = preparedStatement.executeUpdate();
                            if (result == 1) {

                                sql = "DELETE FROM CartItems WHERE Cart = ?";
                                preparedStatement = Database.connection.prepareStatement(sql);
                                preparedStatement.setInt(1, cartId);
                                result = preparedStatement.executeUpdate();

                                if (result == 1) {
                                    data.put("status", "1");
                                } else {
                                    data.put("status", "2");
                                }
                            }
                        }
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

    public static String incrementCartItemQuantity(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String id = req.params(":id");
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        if (id == null) {
            data.put("status", "4");
        } else {
            try {
                if (!id.isEmpty()) {
                    String sql = "UPDATE CartItems SET Quantity = Quantity + 1 WHERE Item = ?";
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

    public static String decrementCartItemQuantity(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String id = req.params(":id");
        HashMap<String, String> request;
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        request = gson.fromJson(req.body(), type);
        HashMap<String, String> data = new HashMap<>();
        if (id == null) {
            data.put("status", "4");
        } else {
            try {
                if (!id.isEmpty()) {
                    String sql = "UPDATE CartItems SET Quantity = Quantity - 1 WHERE Item = ?";
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
}
