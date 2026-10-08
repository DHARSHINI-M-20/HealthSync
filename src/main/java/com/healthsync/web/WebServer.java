package com.healthsync.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
                respond(exchange, 200, "text/html; charset=utf-8", PAGE);
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
            respond(exchange, 500, "application/json; charset=utf-8", "{\"error\":\"" + json(safeMessage(e)) + "\"}");
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
    private static String json(String value) { return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r"); }
    private static void respond(HttpExchange exchange, int status, String type, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", type);
        exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    private static final String PAGE = """
        <!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>HealthSync</title>
        <style>body{font:16px system-ui;background:#f2f7f7;color:#17333a;margin:0}.card{max-width:680px;margin:8vh auto;background:white;padding:32px;border-radius:16px;box-shadow:0 12px 40px #17333a18}h1{margin-top:0;color:#087e78}label{display:block;margin:18px 0 6px;font-weight:600}select,input,textarea,button{box-sizing:border-box;width:100%;font:inherit;padding:12px;border:1px solid #c8d8d8;border-radius:8px}textarea{min-height:140px;resize:vertical}button{margin-top:18px;background:#087e78;color:white;border:0;font-weight:700;cursor:pointer}button:disabled{opacity:.6}small{color:#60777b}</style></head><body><main class="card"><h1>HealthSync</h1><p>Explore the GoF design patterns used in HealthSync.</p><form id="demo"><label for="pattern">Design Pattern</label><select id="pattern" name="pattern"></select><label for="input">Input</label><input id="input" name="input" type="text" placeholder="Enter a demo value"><button id="submit">Execute</button><label for="output">Output</label><textarea id="output" readonly placeholder="Your result will appear here"></textarea><small>Examples use the existing HealthSync pattern classes.</small></form></main>
        <script>const names=['Singleton','Factory Method','Abstract Factory','Builder','Prototype','Adapter','Bridge','Composite','Decorator','Facade','Flyweight','Proxy','Chain of Responsibility','Command','Interpreter','Iterator','Mediator','Memento','Observer','State','Strategy','Template Method','Visitor'];const select=document.querySelector('#pattern');names.forEach(n=>{let o=document.createElement('option');o.value=n;o.textContent=n;select.append(o)});document.querySelector('#demo').addEventListener('submit',async e=>{e.preventDefault();const b=document.querySelector('#submit'),out=document.querySelector('#output');b.disabled=true;out.value='Running…';try{const r=await fetch('/execute',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body:new URLSearchParams(new FormData(e.target))});const data=await r.json();out.value=data.output||data.error||'No output'}catch(err){out.value='Request failed: '+err.message}finally{b.disabled=false}});</script></body></html>
        """;
}
