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
    public static String parseID(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject record = jsonResponse.getJSONObject("record");
        if (record.has("id")) {
            String id = record.getString("id");
            System.out.println(id);
            return id;
        } else {
            System.out.println("ID not found in the response");
            return null;
        }
    }
    public static String[] parseFollowIDs(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        if (jsonResponse.has("followingIDs")) {
            // System.out.println(jsonResponse.getJSONArray("items").length() + " records found in the response");
            String[] followingIDs = jsonResponse.getString("followingIDs").split(",");
            return followingIDs;
        } else {
            System.out.println("Records not found in the response");
            return null;
        
        }
    }
    public static String[] parseEmails(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        if (jsonResponse.has("items")) {
            // System.out.println(jsonResponse.getJSONArray("items").length() + " records found in the response");
            String[] emails = new String[jsonResponse.getJSONArray("items").length()];
            for (int i = 0; i < emails.length; i++) {
                emails[i] = jsonResponse.getJSONArray("items").getJSONObject(i).getString("email");
            }
            return emails;
        } else {
            System.out.println("Records not found in the response");
            return null;
        }
    }
    public static String[] parseUsername(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        if (jsonResponse.has("items")) {
            // System.out.println(jsonResponse.getJSONArray("items").length() + " records found in the response");
            String[] usernames = new String[jsonResponse.getJSONArray("items").length()];
            for (int i = 0; i < usernames.length; i++) {
                usernames[i] = jsonResponse.getJSONArray("items").getJSONObject(i).getString("username");
            }
            return usernames;
        } else {
            System.out.println("Records not found in the response");
            return null;
        }
    }
}

