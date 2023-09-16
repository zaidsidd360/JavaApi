package com.main;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Connection connection;
    static String userName = "root";
    static String password = "root";
    static String databaseName = "spectrumdb";
    static String url = "jdbc:mysql://localhost:3306/" + databaseName;

    public static void main(String[] args) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            IOException, SQLException {

//        DATABASE CONNECTION
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        connection = DriverManager.getConnection(url, userName, password);

//        SERVER CONTEXT CREATION
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/teachers", (exchange) -> {
            if("GET".equals(exchange.getRequestMethod())) {
                String getTeachersQuery = "SELECT * from " + databaseName + ".teachers;";
                ResultSet rs;
                ArrayList<String> list = new ArrayList<>();
                try {
                    PreparedStatement ps = connection.prepareStatement(getTeachersQuery);
                    rs = ps.executeQuery();
                    while(rs.next()) {
                        list.add(rs.getString("teacher_name"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                String response = list.toString();
                exchange.sendResponseHeaders(200, response.length());
                OutputStream stream = exchange.getResponseBody();
                stream.write(response.getBytes());
                stream.flush();
            } else if("POST".equals(exchange.getRequestMethod())) {
                InputStream istream = exchange.getRequestBody();
                Scanner s = new Scanner(istream).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                System.out.println(result);
                JSONObject obj = new JSONObject(result);
                istream.close();
                String message = obj.getString("name") + " added to teachers successfully.";
                exchange.sendResponseHeaders(200, message.length());
                OutputStream ostream = exchange.getResponseBody();
                String insertTeacherQuery = "INSERT INTO teachers (teacher_name) VALUES (\"" + obj.getString("name") + "\")";
                try {
                    PreparedStatement ps = connection.prepareStatement(insertTeacherQuery);
                    ps.executeUpdate();
                    ostream.write(message.getBytes());
                    ostream.flush();
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