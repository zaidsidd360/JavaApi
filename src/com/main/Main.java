package com.main;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Connection connection;
    static String userName = "root";
    static String password = "root";
    static String databaseName = "spectrumdb";
    static String url = "jdbc:mysql://localhost:3306/" + databaseName;

    static JSONObject bodyParams;

    public static void executeQuery(String query, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(query);
        int status = ps.executeUpdate();
        System.out.println(status != 0 ? "Query run successful" : "BRRR");
    }

    public static void main(String[] args) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            SQLException, IOException {

//        DATABASE CONNECTION
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        connection = DriverManager.getConnection(url, userName, password);

//        SERVER CONTEXT CREATION
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/teachers", (exchange) -> {
            if("POST".equals(exchange.getRequestMethod())) {
                InputStream stream = exchange.getRequestBody();
                Scanner s = new Scanner(stream).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                System.out.println(result);
                JSONObject obj = new JSONObject(result);
                stream.close();
                try {
                    System.out.println(obj.getString("name"));
                    String query = "INSERT INTO teachers VALUES(3, \"" + obj.getString("name") + "\")";
                    PreparedStatement ps = connection.prepareStatement(query);
                    System.out.println(query);
                    int status = ps.executeUpdate();
                    if(status != 0)
                        System.out.println("Query run successful");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            exchange.close();
        });
        server.setExecutor(null);
        server.start();
        System.out.println("Service running at http://localhost:" + 8080);
    }
}