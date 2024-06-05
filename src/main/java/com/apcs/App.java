package com.apcs;
import java.io.*;
import java.util.*;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
@SuppressWarnings("unused")
public final class App {
    protected static String currentUserId = "";
    private App() {
    }
    public static void main(String[] args) throws Exception, DotenvException {
        Dotenv dotenv = Dotenv.load();
        Scanner input = new Scanner(System.in);
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        
        boolean signedIn = false;
        while(!signedIn){
            System.out.println("Sign in (I) or Sign up (II)?");
            String choice = input.nextLine();
            if (choice.equals("I")) {
                signedIn = signIn();

            }
            else if (choice.equals("II")) {
                signedIn = signUp();
            }
            else {
                System.out.println("Invalid choice");
            }
        }
        String command = "";
        while(!command.equals("/q")) {
            System.out.print("--> ");
            command = input.nextLine();
            switch(command) {
                case "/q":
                    System.out.println("Goodbye!");
                    break;
                case "/help":
                    System.out.println("Commands: /q (quit), /help (display this message)");
                    break;
                case "/lp":
                    listPosts();
                    break;
                case "/lfr":
                    listFollowers();
                    break;
                case "/lfg":
                    listFollowing();
                    break;
                case "/p":
                    System.out.println("Enter post: ");
                    String post = input.nextLine();
                    byte[] out = String.format("{\"content\":\"%s\",\"author\":\"%s\"}", post, currentUserId).getBytes(StandardCharsets.UTF_8);
                    String url = "https://apcsa.continuityhost.com/api/collections/posts/records";
                    String response = VHTTP.post(url, out, token);
                    System.out.println(response);
                    break;
                case "/f":
                    follow();
                    break;
                default:
                    System.out.println("Invalid command");
            }

        }
    
        
        input.close();
            
    }
    private static void follow() throws IOException {
        Dotenv dotenv = Dotenv.load();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter username to follow: ");
        String username = input.nextLine();
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        String id = VHTTP.parseIDFromUsername(username, token);
        System.out.println(id);
        if (!checkIfShouldFollow(id)) {
            System.out.println("Already following this user");
            return;
        }
        byte[] out = String.format("{\"follower\":\"%s\",\"followee\":\"%s\"}", currentUserId, id).getBytes(StandardCharsets.UTF_8);
        String url = "https://apcsa.continuityhost.com/api/collections/follows/records";
        String response = VHTTP.post(url, out, token);
        System.out.println(response);

    }
    private static boolean checkIfShouldFollow(String id) throws IOException{
        Dotenv dotenv = Dotenv.load();
        String url = "https://apcsa.continuityhost.com/api/collections/follows/records";
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        String response = VHTTP.get(url, token);
        ArrayList<String> following = VHTTP.parseFollowingIDs(response, currentUserId);
        for (String i : following) {
            if (i.equals(id)) {
                return false;
            }
            else {
                continue;
            }
        }
        return true;
    }
    private static void listFollowing() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String url = "https://apcsa.continuityhost.com/api/collections/follows/records/";
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        String response = VHTTP.get(url, token);
        ArrayList<String> following = VHTTP.parseFollowingIDs(response, currentUserId);
        for (String id : following) {
            String user = "https://apcsa.continuityhost.com/api/collections/users/records/"+id;
            String userResponse = VHTTP.get(user, token);
            JSONObject jsonResponse = new JSONObject(userResponse);
            if (jsonResponse.has("username")) {
                String username = jsonResponse.getString("username");
                System.out.println(username);
            } else {
                System.out.println("Username not found in the response");
            }
        }
        // System.out.println(response);
    }
    private static void listFollowers() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String url = "https://apcsa.continuityhost.com/api/collections/follows/records/";
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        String response = VHTTP.get(url, token);
        ArrayList<String> following = VHTTP.parseFollowerIDs(response, currentUserId);
        for (String id : following) {
            String user = "https://apcsa.continuityhost.com/api/collections/users/records/"+id;
            String userResponse = VHTTP.get(user, token);
            JSONObject jsonResponse = new JSONObject(userResponse);
            if (jsonResponse.has("username")) {
                String username = jsonResponse.getString("username");
                System.out.println(username);
            } else {
                System.out.println("Username not found in the response");
            }
        }
    }
    
    private static void listPosts() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String url = "https://apcsa.continuityhost.com/api/collections/posts/records";
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        String response = VHTTP.get(url, token);
        System.out.println(response);
    }
    private static String[] parseEmails() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String url = "https://apcsa.continuityhost.com/api/collections/users/records";
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        String response = VHTTP.get(url, token);
        // System.out.println(response);
        return VHTTP.parseEmails(response);
    }
    private static String[] parseUsernames() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String url = "https://apcsa.continuityhost.com/api/collections/users/records";
        String token = getAuthToken(dotenv.get("AUTH_URL"), dotenv.get("ADMIN_IDENTITY"), dotenv.get("ADMIN_SECRET"));
        String response = VHTTP.get(url, token);
        // System.out.println(response);
        return VHTTP.parseUsername(response);
    }
    @SuppressWarnings("resource")
    private static boolean signUp() throws IOException {
        String[] emails = parseEmails();
        String[] usernames = parseUsernames();
        Scanner input = new Scanner(System.in);
        System.out.println("Email: ");
        String email = input.nextLine();

        for (String e : emails) {
            if (e.equals(email)) {
                System.out.println("Email already exists");
                return false;
            }
        }
        System.out.println("Password: ");  
        String password = input.nextLine();
        System.out.println("Confirm Password: ");
        String confirm = input.nextLine();
        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match");
            return false;
        }
        System.out.println("Name: ");
        String name = input.nextLine();
        System.out.println("Username: ");
        String username = input.nextLine();
        for (String u : usernames) {
            if (u.equals(username)) {
                System.out.println("Username already exists");
                return false;
            }
        }
        byte[] out = String.format("{\"email\":\"%s\",\"password\":\"%s\", \"passwordConfirm\":\"%s\",\"name\":\"%s\",\"username\":\"%s\",\"verified\":\"true\"}", email, password, confirm, name, username).getBytes(StandardCharsets.UTF_8);
        String signup = "https://apcsa.continuityhost.com/api/collections/users/records";
        try {
            String response = VHTTP.post(signup, out);
            System.out.println(response);
            return true;
        }
        catch (NullPointerException e) {
            System.out.println("Failed to sign up");
            return false;
        }
    }
    @SuppressWarnings("resource")
    private static boolean signIn() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Email: ");
        String email = input.nextLine();
        System.out.println("Password: ");  
        String password = input.nextLine();

        byte[] out = String.format("{\"identity\":\"%s\",\"password\":\"%s\"}", email, password).getBytes(StandardCharsets.UTF_8);
        String signin = "https://apcsa.continuityhost.com/api/collections/users/auth-with-password";
        try {
            String response = VHTTP.post(signin, out);
            System.out.println(response);
            String userToken = VHTTP.parseToken(response);
            currentUserId = VHTTP.parseID(response);
            System.out.println(userToken);
            return true;
        }
        catch (NullPointerException e) {
            System.out.println("Failed to sign in");
            return false;
        }    
    }
    private static String getAuthToken(String AUTH_URL, String ADMIN_ID, String ADMIN_PASS) throws IOException {
            byte[] out = String.format("{\"identity\":\"%s\",\"password\":\"%s\"}", ADMIN_ID, ADMIN_PASS)
                .getBytes(StandardCharsets.UTF_8);
            String responseString = VHTTP.post(AUTH_URL, out);
            // Parse the JSON response to extract the token
            return VHTTP.parseToken(responseString);
    }
}
