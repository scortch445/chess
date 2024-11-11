package ui;

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

        var http = (HttpURLConnection) new URI(base_url+"/user").toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out a header
        http.addRequestProperty("Content-Type", "application/json");

        // Write out the body
        var body = Map.of("username", userData.username(), "password", userData.password(),"email",userData.email());
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        http.connect();

        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            authData = new Gson().fromJson(inputStreamReader, AuthData.class);
            System.out.println(authData);
        }

        return authData;
    }

    public AuthData login(UserData userData) {
        return null;
    }

    public void logout(String authToken) {

    }

    public ArrayList<GameData> getGames(String authToken) {
        return null;
    }

    public int createGame(String authToken, String gameName){
        return 0;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }

}
