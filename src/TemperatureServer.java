/*
  Autheur     : Bilel Belhadj
  Description : create java server to handle http POST and GET request
  Date        : 02-04-2023
  Version     : 0.0.1
  Source      : https://www.youtube.com/watch?v=FNUdLeGfShU&list=PLAuGQNR28pW56GigraPdiI0oKwcs8gglW
                https://www.postgresqltutorial.com/?s=java
*/


import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpServer;

public class TemperatureServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);  //create server on port 8080

        // Bind the "/temperature" endpoint to a handler that saves the temperature data to a variable
        server.createContext("/temperature", new TemperatureHandler());

        // Start the server
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();

        System.out.println("Server started on port 8080");  //print confirmation message
    }
}
