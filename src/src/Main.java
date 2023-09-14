package src;
//import com.sun.net.httpserver.HttpServer;
//import java.net.InetSocketAddress;
//import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    static Connection connection = null;
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
            SQLException
    {

        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        connection = DriverManager.getConnection(url, userName, password);

        PreparedStatement ps = connection.prepareStatement("INSERT INTO teachers VALUES(2, \"Arif Boss\")");
        int status = ps.executeUpdate();
        if(status != 0) System.out.println("Query run successful");
    }
}

/*
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new Handler("/", "Hello there!"));
        server.setExecutor(null);
        server.start();
        System.out.println("Service running at http://localhost:" + 8080);
*/