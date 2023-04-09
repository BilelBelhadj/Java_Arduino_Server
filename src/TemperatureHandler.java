/* Class to manipulate data */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class TemperatureHandler implements HttpHandler {
    float temperature = 0;
    float humidite = 0;
    String device = "E1P1";

    long id = 0;
    
    
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String tempStrData = "1", humStrData = "2", deviceStrData = "3";
        postgresql database = new postgresql();
        String requestMethod = exchange.getRequestMethod();

        //deal with post request
        if (requestMethod.equalsIgnoreCase("POST")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));   // Read the request body as json
            String requestBody = reader.readLine();
            reader.close();     //close connection

            // decode the json variable and extract data
            try {
                JSONObject json = new JSONObject(requestBody);      //create json object
                temperature = json.getFloat("temperature");     //extract temperature
                humidite = json.getFloat("humidite");           //extract humidity
                device = json.getString("device");              //extract device name

                id = database.insertData(device, "TMP", temperature);    //insert data into the database
                id = database.insertData(device, "HUM", humidite);       

                System.out.printf("Received temperature data: %.2f, humidite data: %.2f, device: %s\n", tempStrData, humStrData, deviceStrData);
            } catch (JSONException e) {
                System.err.println("Invalid JSON format: " + requestBody);  //dealing with json exeption 
            }
        }

        String[] dataTmp = database.getTmpData(device); //read latest temperature
        String[] dataHum = database.getHumData(device); //read latest humidity
        tempStrData = dataTmp[0];                       //fill variables with data
        humStrData = dataHum[0];
       
        //print data to serial monitor
        System.out.println("affichage handler");
        System.out.println(tempStrData);
        System.out.println(humStrData);

        //build thew json variables and print it
        System.out.println("json **********************");  
        String response = String.format("{\"temperature\": \"%s\", \"humidite\": \"%s\", \"device\": \"%s\"}", tempStrData, humStrData, device);
        System.out.println(response);
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");            //authorise the web page to interact with the server

        exchange.sendResponseHeaders(200, response.getBytes().length);  //send the json
        exchange.getResponseBody().write(response.getBytes());                //get the response  
        exchange.close();   //close the connection
    }
    
}
