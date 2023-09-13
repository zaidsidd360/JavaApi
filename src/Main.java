import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class Main {
    static Connection connection;
    static String userName = "root";
    static String password = "root";


    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new Handler("/", "Hello there!"));

        server.setExecutor(null);
        server.start();

        System.out.println("Service running at http://localhost:" + 8080);
    }
}