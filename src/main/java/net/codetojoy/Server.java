
package net.codetojoy;

import com.sun.net.httpserver.*;

import java.io.*;
import java.util.*;
import java.net.*;

public class Server {

  public static void main(String[] args) throws IOException {
      var port = 5151;
      var server = HttpServer.create(new InetSocketAddress(port), 0);
      var context = server.createContext("/waro");
      context.setHandler(Server::handleRequest);
      System.out.println("TRACER listening on port: " + port);
      server.start();
  }

  private static void handleRequest(HttpExchange exchange) throws IOException {
      String response = "Hi there!";
      exchange.sendResponseHeaders(200, response.getBytes().length);
      OutputStream os = exchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
  }
}
