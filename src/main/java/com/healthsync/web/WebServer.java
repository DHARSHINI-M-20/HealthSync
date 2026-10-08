package com.healthsync.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Executors;

/** Browser presentation layer for selected, database-independent pattern examples. */
public final class WebServer {
    private WebServer() { }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "10000"));
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/", WebServer::route);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("HealthSync web server listening on port " + port);
    }

    private static void route(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        try {
            if ("GET".equals(exchange.getRequestMethod()) && "/".equals(path)) {
                respond(exchange, 200, "text/html; charset=utf-8", resource("index.html"));
            } else if ("GET".equals(exchange.getRequestMethod()) && "/styles.css".equals(path)) {
                respond(exchange, 200, "text/css; charset=utf-8", resource("styles.css"));
            } else if ("GET".equals(exchange.getRequestMethod()) && "/app.js".equals(path)) {
                respond(exchange, 200, "text/javascript; charset=utf-8", resource("app.js"));
            } else if ("GET".equals(exchange.getRequestMethod()) && "/health".equals(path)) {
                respond(exchange, 200, "application/json; charset=utf-8", "{\"status\":\"ok\",\"service\":\"HealthSync\"}");
            } else if ("POST".equals(exchange.getRequestMethod()) && "/execute".equals(path)) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> form = parseForm(body);
                String result;
                try { result = PatternDemos.run(form.getOrDefault("pattern", ""), form.getOrDefault("input", "")); }
                catch (Exception e) { result = "Demonstration error: " + safeMessage(e); }
                respond(exchange, 200, "application/json; charset=utf-8", "{\"output\":\"" + json(result) + "\"}");
            } else {
                respond(exchange, 404, "application/json; charset=utf-8", "{\"error\":\"Not found\"}");
            }
        } catch (Exception e) {
            respond(exchange, 500, "application/json; charset=utf-8", "{\"error\":\"Request could not be completed.\"}");
        } finally { exchange.close(); }
    }

    private static Map<String, String> parseForm(String body) {
        java.util.Map<String, String> values = new java.util.HashMap<>();
        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) values.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8), URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
        }
        return values;
    }
    private static String safeMessage(Exception e) { return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage(); }
    private static String resource(String name) throws IOException {
        try (InputStream stream = WebServer.class.getResourceAsStream("/web/" + name)) {
            if (stream == null) throw new IOException("Web resource unavailable");
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    private static String json(String value) { return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r"); }
    private static void respond(HttpExchange exchange, int status, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
    }
}
