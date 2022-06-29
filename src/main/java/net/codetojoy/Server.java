
// note: i no longer own this domain
package net.codetojoy;

import com.sun.net.httpserver.*;

import java.io.*;
import java.util.*;
import java.net.*;

// Q: no tests?
// A: i will probably regret it but this http layer is meant to be quick & dirty. 
//    it may or may not be quick, but it is certainly dirty: achievement unlocked.
//
// Q: no framework?
// A: it is hard to say what is currently supported for JDK 19, so I'm rolling my own for now 
//    The real point here is to understand `sealed interface`/records in JDK 19, and this file is
//    just undifferentiated heavy-lifting.

record Param(String key, String value) {}

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
        var params = getParams(exchange);
        var response = new StrategyService().apply(params);
        sendResponse(response, exchange);
    }

    private static void sendResponse(String response, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (var os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private static List<Param> getParams(HttpExchange exchange) throws IOException {
        var uri = exchange.getRequestURI();
        var query = uri.getQuery();   
        System.out.println("TRACER query: " + query);

        var params = new ArrayList<Param>();
        var tokens = query.split("&");

        for (var token : tokens) {
            var pair = token.split("=");
            var param = new Param(pair[0], pair[1]);
            params.add(param);
        }

        return params;
    }

    private static void handleEcho(HttpExchange exchange) throws IOException {
        var params = getParams(exchange);

        var response = new StringBuilder();
        for (var param : params) {
            response.append(" " + param.key() + " : " + param.value() + " , ");
        }
        response.append("\n");

        sendResponse(response.toString(), exchange);
    }
}
