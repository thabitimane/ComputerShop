package com.computer.shop;

import com.computer.shop.database.Database;
import com.computer.shop.routes.Routes;

import java.io.File;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        Database.connect();
        externalStaticFileLocation(new File("").getAbsolutePath()+"/files");
        port(8080);
        Routes.routes();
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }

}
