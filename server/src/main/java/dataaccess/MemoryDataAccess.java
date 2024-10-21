package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryDataAccess implements DataAccess {
    private ArrayList<UserData> users = new ArrayList<>();
    private ArrayList<AuthData> authDatas = new ArrayList<>();
    private ArrayList<GameData> games = new ArrayList<>();
    int nextGameID = 1;


    @Override
    public UserData getUser(String username) {
        for(UserData user : users){
            if(Objects.equals(user.username(), username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void saveUser(UserData userData) {
        users.add(userData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        for(AuthData authData : authDatas){
            if(Objects.equals(authData.authToken(), authToken)){
                return authData;
            }
        }
        return null;
    }

    @Override
    public void saveAuth(AuthData authData) {
        authDatas.add(authData);
    }

    @Override
    public void deleteAuth(AuthData authData) {
        authDatas.remove(authData);
    }

    @Override
    public ArrayList<GameData> getGames() {
        return games;
    }

    @Override
    public void createGame(GameData gameData) {
        games.add(gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        for(GameData game : games){
            if(Objects.equals(game.gameID(), gameID)){
                return game;
            }
        }
        return null;
    }

    @Override
    public void saveGame(GameData updatedGame) {
        // First remove the previous gameData
        for(GameData game : games){
            if(Objects.equals(game.gameID(), updatedGame.gameID())){
                games.remove(game);
                break;
            }
        }

        games.add(updatedGame);

    }

    @Override
    public void clear() {
        users.clear();
        authDatas.clear();
        games.clear();
    }
}
