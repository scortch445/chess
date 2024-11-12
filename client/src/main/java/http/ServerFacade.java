package http;

import com.google.gson.Gson;
import model.*;
import request.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {

    private String base_url;

    public ServerFacade(int port){
        base_url = "http://localhost:" + port;
    }

    public AuthData register(UserData userData) throws Exception{
        AuthData authData;
        var body = Map.of("username", userData.username(), "password", userData.password(),"email",userData.email());

        var http = sendMessage("/user","POST",body,null);

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            authData = new Gson().fromJson(inputStreamReader, AuthData.class);
            System.out.println(authData);
        }

        return authData;
    }

    public AuthData login(UserData userData) throws Exception{
        AuthData authData;

        var body = Map.of("username", userData.username(), "password", userData.password());

        var http = sendMessage("/session","POST",body,null);

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            authData = new Gson().fromJson(inputStreamReader, AuthData.class);
            System.out.println(authData);
        }

        return authData;
    }

    public void logout(String authToken) throws Exception {

        sendMessage("/session","DELETE",null,authToken);
    }

    public ArrayList<GameData> getGames(String authToken) {
        return null;
    }

    public int createGame(String authToken, String gameName){
        return 0;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }

    private HttpURLConnection sendMessage(String path, String httpRequestMethod, Map body, String authToken) throws Exception {
        var http = (HttpURLConnection) new URI(base_url+path).toURL().openConnection();
        http.setRequestMethod(httpRequestMethod);

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");
        if(authToken!=null){
            http.addRequestProperty("authorization",authToken);
        }

        // Write out the body
        if(body!=null) {
            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }
        }

        http.connect();

        // handle any error brought up in the response
        int statusCode = http.getResponseCode();

        if(statusCode!=200){
            throw new ResponseException(statusCode, http.getResponseMessage());
        }

        return http;
    }

}

