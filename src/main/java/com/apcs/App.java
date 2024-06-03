package com.apcs;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
public final class App {
    private App() {
    }
    public static void main(String[] args) throws Exception, DotenvException {
        Dotenv dotenv = Dotenv.load();
        Scanner input = new Scanner(System.in);
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        if (token != null) {
            String response = HTTP.get(dotenv.get("POSTS_URL"), token);
            System.out.println(response);
        } else {
            System.out.println("Failed to retrieve token");
        }
        System.out.println("Email: ");
        String email = input.nextLine();
        System.out.println("Password: ");  
        String password = input.nextLine();

        byte[] out = String.format("{\"identity\":\"%s\",\"password\":\"%s\"}", email, password).getBytes(StandardCharsets.UTF_8);
        String signin = "https://apcsa.continuityhost.com/api/collections/users/auth-with-password";
        try {
            String response = HTTP.post(signin, out);
            String userToken = HTTP.parseToken(response);
            System.out.println(userToken);
        }
        catch (NullPointerException e) {
            System.out.println("Failed to sign in");
        }
        input.close();
            
    }

    private static String getAuthToken(String AUTH_URL, String ADMIN_ID, String ADMIN_PASS) throws IOException {
            byte[] out = String.format("{\"identity\":\"%s\",\"password\":\"%s\"}", ADMIN_ID, ADMIN_PASS)
                .getBytes(StandardCharsets.UTF_8);
            String responseString = HTTP.post(AUTH_URL, out);
            // Parse the JSON response to extract the token
            return HTTP.parseToken(responseString);
    }
}
