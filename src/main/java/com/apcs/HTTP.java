package com.apcs;

import java.io.*;
import java.net.*;

import org.json.JSONObject;


public class HTTP {
    public HTTP() {
    }
    public static String post(String url, byte[] json) throws IOException {
        URL con = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) con.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        int length = json.length;
        connection.setFixedLengthStreamingMode(length);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.connect();

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json);
        }
        int responseCode = connection.getResponseCode();
        System.out.println("POST Response Code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseString = response.toString();
            return responseString;
        } else {
            System.out.println("POST request failed. Please try again.");
            return null;
        }
         
    }
    public static String get(String url) throws IOException {
        URL con = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) con.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        System.out.println("GET Response Code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseString = response.toString();
            return responseString;
        } else {
            System.out.println("GET request failed. Please try again.");
            return null;
        }
    }
    public static String get(String url, String token) throws IOException {
        URL con = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) con.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.connect();
        int responseCode = connection.getResponseCode();
        System.out.println("GET Response Code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String responseString = response.toString();
            return responseString;
        } else {
            System.out.println("GET request failed. Please try again.");
            return null;
        }
    }
    public static String parseToken(String response) {
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("token")) {
                String token = jsonResponse.getString("token");
                return token;
            } else {
                System.out.println("Token not found in the response");
                return null;
            }
    } 
}

