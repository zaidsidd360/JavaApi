package com.main;

import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainJsonSimple {
    static Connection connection;
    static String userName = "root";
    static String password = "root1";
    static String databaseName = "spectrumdb";
    static String url = "jdbc:mysql://localhost:3306/" + databaseName;

//    @SuppressWarnings("unchecked")
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
            System.out.println("In Server Context");
            System.out.println(exchange.getRequestMethod());
            final String origin = exchange.getRequestHeaders().getFirst("Origin");
            if(origin != null) exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173/");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
            exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            exchange.getResponseHeaders().set("contentType", "application/json; charset=UTF-8");
            if("GET".equals(exchange.getRequestMethod())) {
                String getTeachersQuery = "SELECT * FROM " + databaseName + ".teachers;";
                ResultSet rs;
                ArrayList<HashMap<String, Object>> teachersList = new ArrayList<>();
                try {
                    PreparedStatement ps = connection.prepareStatement(getTeachersQuery);
                    rs = ps.executeQuery();
                    while(rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("teacher_name");
                        String email = rs.getString("email");
                        int phone = rs.getInt("phone_number");
                        HashMap<String, Object> teacher = new HashMap<>();
                        teacher.put("id", id);
                        teacher.put("name", name);
                        teacher.put("email", email);
                        teacher.put("phone", phone);
                        teachersList.add(teacher);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                JSONArray respArr = new JSONArray();
                respArr.addAll(teachersList);
                JSONObject respObj = new JSONObject();
                respObj.put("teachers", respArr);
                exchange.sendResponseHeaders(200, respObj.toString().getBytes().length);
                OutputStream stream = exchange.getResponseBody();
                stream.write(respObj.toString().getBytes());
                stream.flush();
            } else if("POST".equals(exchange.getRequestMethod())) {
                System.out.println("Here in POST Block");
                InputStream istream = exchange.getRequestBody();
                Scanner s = new Scanner(istream).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                System.out.println(result);
                JSONObject obj = (JSONObject) JSONValue.parse(result);
                istream.close();
                String message = obj.get("name") + " added to teachers successfully.";
                exchange.sendResponseHeaders(200, message.length());
                OutputStream ostream = exchange.getResponseBody();
                String name = (String) obj.get("name");
                String email = (String) obj.get("email");
                Object phone = obj.get("phone");

                String insertTeacherQuery =
                        "INSERT INTO " + databaseName + ".teachers (teacher_name, phone_number, email) " +
                        "VALUES (\"" + name + "\", " + phone + ", \"" + email + "\")";
                try {
                    PreparedStatement ps = connection.prepareStatement(insertTeacherQuery);
                    ps.executeUpdate();
                    ostream.write(message.getBytes());
                    ostream.flush();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                InputStream istream = exchange.getRequestBody();
                Scanner s = new Scanner(istream).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                JSONObject obj = (JSONObject) JSONValue.parse(result);
                istream.close();
                String message = obj.get("name") + " removed from teachers successfully.";
                exchange.sendResponseHeaders(200, message.length());
                OutputStream ostream = exchange.getResponseBody();
                String id =  obj.get("id").toString();
                String deleteTeacherQuery = "DELETE FROM " + databaseName + ".teachers WHERE id = " + id + ";";
                try {
                    PreparedStatement ps = connection.prepareStatement(deleteTeacherQuery);
                    ps.executeUpdate();
                    ostream.write(message.getBytes());
                    ostream.flush();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if ("OPTIONS".equals(exchange.getRequestMethod())) {
                String message = "Success";
                exchange.sendResponseHeaders(200, message.length());
                OutputStream ostream = exchange.getResponseBody();
                ostream.write(message.getBytes());
                ostream.flush();
            }
            exchange.close();
        });
        server.createContext("/api/students", (exchange) -> {
            if("GET".equals(exchange.getRequestMethod())) {
                String getStudentsQuery = "SELECT * FROM spectrumdb.students;";
                ResultSet rs;
                try {
                    PreparedStatement ps = connection.prepareStatement(getStudentsQuery);
                    rs = ps.executeQuery();
                    System.out.println(rs);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        server.setExecutor(null);
        server.start();
        System.out.println("Service running at http://localhost:" + 8080 + "/api/teachers");
    }
}
