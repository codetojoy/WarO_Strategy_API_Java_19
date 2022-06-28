
package net.codetojoy;

import com.sun.net.httpserver.*;

import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        var port = 5151;
        var server = HttpServer.create(new InetSocketAddress(port), 0);
        var c1 = server.createContext("/ping");
        c1.setHandler(Server::handlePing);
        var c2 = server.createContext("/waro");
        c2.setHandler(Server::handleWaro);
        var c3 = server.createContext("/echo");
        c3.setHandler(Server::handleEcho);
        System.out.println("TRACER listening on port: " + port);
        server.start();
    }

    private static void handlePing(HttpExchange exchange) throws IOException {
        String response = "TRACER date: " + new Date().toString();
        sendResponse(response, exchange);
        exchange.sendResponseHeaders(200, response.getBytes().length);
    }

    // prize_card=10&max_card=12&mode=max&cards=4&cards=6&cards=2
    private static void handleWaro(HttpExchange exchange) throws IOException {
        String response = "Hi from waro!";
        sendResponse(response, exchange);
    }

    private static void sendResponse(String response, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private static List<String> getParams(HttpExchange exchange) throws IOException {
        var uri = exchange.getRequestURI();
        var query = uri.getQuery();   
        System.out.println("TRACER query: " + query);

        var params = new ArrayList<String>();
        var tokens = query.split("&");

        for (var token : tokens) {
            params.add(token);
        }

        return params;
    }

    private static void handleEcho(HttpExchange exchange) throws IOException {
        var params = getParams(exchange);

        var response = new StringBuilder();
        for (var param : params) {
            var pair = param.split("=");
            var key = pair[0].trim();
            var value = pair[1].trim();
            response.append(" " + key + " : " + value + " , ");
        }
        response.append("\n");

        sendResponse(response.toString(), exchange);
    }
}
