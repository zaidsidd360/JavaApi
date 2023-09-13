import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class Main {
    static Connection connection = null;
    static String userName = "root";
    static String password = "root";
    static String databaseName = "spectrum-db";
    static String url = "jdbc:mysql://localhost:3306/" + databaseName;


    public static void main(String[] args) throws
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException
    {
/*
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new Handler("/", "Hello there!"));
        server.setExecutor(null);
        server.start();
        System.out.println("Service running at http://localhost:" + 8080);
*/
        Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
    }
}