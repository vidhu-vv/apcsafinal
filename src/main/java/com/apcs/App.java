package com.apcs;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public final class App {
    private App() {
    }
    public static void main(String[] args) throws Exception, DotenvException {
        Dotenv dotenv = Dotenv.load();

        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        if (token != null) {
            sendGetRequest(token, dotenv.get("POSTS_URL"));
        } else {
            System.out.println("Failed to retrieve token");
        }
    }

    private static String getAuthToken(String AUTH_URL, String ADMIN_ID, String ADMIN_PASS) throws IOException {
        @SuppressWarnings("deprecation")
        URL url = new URL(AUTH_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        byte[] out = String.format("{\"identity\":\"%s\",\"password\":\"%s\"}", ADMIN_ID, ADMIN_PASS)
                .getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        connection.setFixedLengthStreamingMode(length);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.connect();

        try (OutputStream os = connection.getOutputStream()) {
            os.write(out);
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

            // Print the entire response for debugging purposes
            String responseString = response.toString();
            System.out.println("Response: " + responseString);

            // Parse the JSON response to extract the token
            JSONObject jsonResponse = new JSONObject(responseString);
            if (jsonResponse.has("token")) {
                String token = jsonResponse.getString("token");
                System.out.println("Extracted Token: " + token);
                return token;
            } else {
                System.out.println("Token not found in the response");
                return null;
            }
        } else {
            System.out.println("POST request not worked");
            return null;
        }
    }

    private static void sendGetRequest(String token, String GET_URL) throws IOException {
        @SuppressWarnings("deprecation")
        URL url = new URL(GET_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);

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

            System.out.println("GET Response: " + response.toString());
        } else {
            System.out.println("GET request not worked");
        }
    }
}
