package com.computer.shop.database;

import com.computer.shop.settings.Settings;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connection = null;

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + Settings.databaseHost + ":" + Settings.databasePort + "/" + Settings.databaseName+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", Settings.databaseUser, Settings.databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (connection == null) ;
    }
}
