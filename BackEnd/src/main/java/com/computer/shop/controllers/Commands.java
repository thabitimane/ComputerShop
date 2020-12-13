package com.computer.shop.controllers;

import com.computer.shop.database.Database;
import com.computer.shop.models.Category;
import com.computer.shop.models.Command;
import com.computer.shop.models.CommandItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;
import spark.Response;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Commands {

    public static String getCommands(Request req, Response res) {
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
                    String sql = "SELECT * FROM Commands";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Command> commands = new ArrayList<>();
                    while (resultSet.next()) {
                        Command command = new Command(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                        commands.add(command);
                    }
                    return gson.toJson(commands);
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

    public static String getCommandItems(Request req, Response res) {
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
                    String sql = "SELECT * FROM CommandItems WHERE Command = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<CommandItem> commandItems = new ArrayList<>();
                    while (resultSet.next()) {
                        CommandItem commandItem = new CommandItem(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4));
                        commandItems.add(commandItem);
                    }
                    return gson.toJson(commandItems);
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

    public static String getPendingCommands(Request req, Response res) {
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
                    String sql = "SELECT * FROM Commands WHERE Status = 'pending'";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Command> commands = new ArrayList<>();
                    while (resultSet.next()) {
                        Command command = new Command(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                        commands.add(command);
                    }
                    return gson.toJson(commands);
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

    public static String getPendingCommandsForUser(Request req, Response res) {
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
                    String sql = "SELECT * FROM Commands WHERE User = ? AND Status = 'pending'";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Command> commands = new ArrayList<>();
                    while (resultSet.next()) {
                        Command command = new Command(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                        commands.add(command);
                    }
                    return gson.toJson(commands);
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

    public static String processCommand(Request req, Response res) {
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
                    String sql = "UPDATE Commands SET Status = 'processed' WHERE Id = ?";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    int result = preparedStatement.executeUpdate();
                    if (result == 1) {
                        data.put("status", "1");
                    } else {
                        data.put("status", "2");
                    }
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

    public static String getProcessedCommands(Request req, Response res) {
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
                    String sql = "SELECT * FROM Commands WHERE Status = 'processed'";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Command> commands = new ArrayList<>();
                    while (resultSet.next()) {
                        Command command = new Command(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                        commands.add(command);
                    }
                    return gson.toJson(commands);
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

    public static String getProcessedCommandsForUser(Request req, Response res) {
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
                    String sql = "SELECT * FROM Commands WHERE User = ? AND Status = 'processed'";
                    PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ArrayList<Command> commands = new ArrayList<>();
                    while (resultSet.next()) {
                        Command command = new Command(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
                        commands.add(command);
                    }
                    return gson.toJson(commands);
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
}
