package com.apcs;

import java.io.*;
import java.net.*;
import org.json.JSONObject;
import java.util.*;
@SuppressWarnings("deprecation")

public class VHTTP {
    public VHTTP() {
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
        // System.out.println("POST Response Code: " + responseCode);
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
            // System.out.println("POST request failed. Please try again.");
            return null;
        }
         
    }
    public static String post(String url, byte[] json, String token) throws IOException {
        URL con = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) con.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoOutput(true);
        int length = json.length;
        connection.setFixedLengthStreamingMode(length);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.connect();

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json);
        }
        int responseCode = connection.getResponseCode();
        // System.out.println("POST Response Code: " + responseCode);
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
            // System.out.println("POST request failed. Please try again.");
            return null;
        }
         
    }
    public static String get(String url) throws IOException {
        URL con = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) con.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        // System.out.println("GET Response Code: " + responseCode);
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
            // System.out.println("GET request failed. Please try again.");
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
        // System.out.println("GET Response Code: " + responseCode);
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
            // System.out.println("GET request failed. Please try again.");
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
            // System.out.println(id);
            return id;
        } else {
            System.out.println("ID not found in the response");
            return null;
        }
    }
    public static String parseIDFromUsername(String username, String token) {
        String url = "https://apcsa.continuityhost.com/api/collections/users/records";
        try {
            String response = VHTTP.get(url, token);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("items")) {
                // System.out.println(jsonResponse.getJSONArray("items").length() + " records found in the response");
                // System.out.println(jsonResponse.getJSONArray("items"));
                for (int i = 0; i < jsonResponse.getJSONArray("items").length(); i++) {
                    if (jsonResponse.getJSONArray("items").getJSONObject(i).getString("username").equals(username)) {
                        String id = jsonResponse.getJSONArray("items").getJSONObject(i).getString("id");
                        return id;
                    }
                }
                System.out.println("Username not found in the response");
                return null;
            } else {
                System.out.println("Records not found in the response");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
    public static ArrayList<String> parseFollowingIDs(String response, String id) {
        JSONObject jsonResponse = new JSONObject(response);
        ArrayList<String> followingIDs = new ArrayList<String>();
        if (jsonResponse.has("items")) {
            // System.out.println(jsonResponse.getJSONArray("items").length() + " records found in the response");
            for (int i = 0; i < jsonResponse.getJSONArray("items").length(); i++) {
                if(jsonResponse.getJSONArray("items").getJSONObject(i).getString("follower").equals(id)){
                    followingIDs.add(jsonResponse.getJSONArray("items").getJSONObject(i).getString("followee"));
                }
                else {
                    continue;
                }
            }
        } else {
            System.out.println("Records not found in the response");
            return null;
        }
        return followingIDs;         
    }
    public static ArrayList<String> parseFollowerIDs(String response, String id) {
        JSONObject jsonResponse = new JSONObject(response);
        ArrayList<String> followingIDs = new ArrayList<String>();
        if (jsonResponse.has("items")) {
            // System.out.println(jsonResponse.getJSONArray("items").length() + " records found in the response");
            for (int i = 0; i < jsonResponse.getJSONArray("items").length(); i++) {
                if(jsonResponse.getJSONArray("items").getJSONObject(i).getString("followee").equals(id)){
                    followingIDs.add(jsonResponse.getJSONArray("items").getJSONObject(i).getString("follower"));
                }
                else {
                    continue;
                }
            }
        } else {
            System.out.println("Records not found in the response");
            return null;
        }
        return followingIDs;         
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
    public static String getUsernameFromID(String id, String token) {
        String url = "https://apcsa.continuityhost.com/api/collections/users/records";
        try {
            String response = VHTTP.get(url, token);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("items")) {
                // System.out.println(jsonResponse.getJSONArray("items").length() + " records found in the response");
                // System.out.println(jsonResponse.getJSONArray("items"));
                for (int i = 0; i < jsonResponse.getJSONArray("items").length(); i++) {
                    if (jsonResponse.getJSONArray("items").getJSONObject(i).getString("id").equals(id)) {
                        String username = jsonResponse.getJSONArray("items").getJSONObject(i).getString("username");
                        return username;
                    }
                }
                System.out.println("ID not found in the response");
                return null;
            } else {
                System.out.println("Records not found in the response");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            return null;
        }
    }
}

