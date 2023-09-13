import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class Handler implements HttpHandler {

    String route;
    String response;

    public Handler(String route, String response) {
        this.route = route;
        this.response = response;
    }

    private void sendResponse(String response, int statusCode, HttpExchange e) throws IOException {
        String r = response;
        e.sendResponseHeaders(statusCode, r.getBytes().length);
        OutputStream stream = e.getResponseBody();
        stream.write(r.getBytes());
        stream.flush();
    }

    @Override
    public void handle(HttpExchange e) throws IOException {
        String method = e.getRequestMethod();
        switch (method) {
            case "GET": {
                sendResponse(this.response + "\n\nRoute: " + this.route + "\n\nMethod: " + method, 200, e);
            }
            case "POST": {
                sendResponse(this.response + "\n\nRoute: " + this.route + "\n\nMethod: " + method, 200, e);
            }
            case "PUT": {
                sendResponse(this.response + "\n\nRoute: " + this.route + "\n\nMethod: " + method, 200, e);
            }
            case "PATCH": {
                sendResponse(this.response + "\n\nRoute: " + this.route + "\n\nMethod: " + method, 200, e);
            }
            case "DELETE": {
                sendResponse(this.response + "\n\nRoute: " + this.route + "\n\nMethod: " + method, 200, e);
            }
        }
        e.close();
    }

}
