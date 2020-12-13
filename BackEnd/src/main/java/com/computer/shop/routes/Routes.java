package com.computer.shop.routes;

import com.computer.shop.controllers.*;
import com.google.gson.Gson;

import java.util.HashMap;

import static spark.Spark.*;

public class Routes {
    public static void routes() {
        post("/login", Authentication::login);
        post("/register", Authentication::register);

        get("/categories/:id", Categories::getCategory);
        get("/categories", Categories::getCategories);
        post("/categories", Categories::postCategory);
        delete("/categories/:id", Categories::deleteCategory);

        get("/products/:id", Products::getProduct);
        get("/products/search/:query", Products::searchForProducts);
        get("/products", Products::getProducts);
        post("/products", Products::postProduct);
        delete("/products/:id", Products::deleteProduct);

        get("/commands/:id", Commands::getCommands);
        get("/commands/:id/items", Commands::getCommandItems);
        get("/pending/commands", Commands::getPendingCommands);
        get("/processed/commands", Commands::getProcessedCommands);
        get("/commands/pending/:id", Commands::getPendingCommandsForUser);
        get("/commands/process/:id", Commands::processCommand);
        get("/commands/processed/:id", Commands::getProcessedCommandsForUser);
        get("/commands", Commands::getCommands);

        get("/carts/:id", Carts::getCartItems);
        post("/carts", Carts::postCartItem);
        post("/carts/checkout", Carts::checkout);
        get("/carts/:id/increment", Carts::incrementCartItemQuantity);
        get("/carts/:id/decrement", Carts::decrementCartItemQuantity);
        delete("/carts/:id", Carts::deleteCartItem);

        get("/users/:id", Users::getUserById);
        get("/users/verify/:token", Users::verifyUserEmail);
        get("/users", Users::getUsers);
        post("/users", Users::postUser);
        delete("/users/:id", Users::deleteUser);
        put("/users/:id", Users::updateUser);

        notFound((req, res) -> {
            res.type("application/json");
            res.status(404);
            Gson gson = new Gson();
            HashMap<String, String> data = new HashMap<>();
            data.put("message", "Not Found");
            return gson.toJson(data);
        });

        get("/", (req, res) -> {
            res.type("application/json");
            Gson gson = new Gson();
            HashMap<String, String> data = new HashMap<>();
            data.put("message", "working");
            return gson.toJson(data);
        });
    }
}
