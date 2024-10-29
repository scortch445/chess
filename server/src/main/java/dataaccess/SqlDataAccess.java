package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class SqlDataAccess implements DataAccess {
    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void saveUser(UserData userData) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void saveAuth(AuthData authData) {

    }

    @Override
    public void deleteAuth(AuthData authData) {

    }

    @Override
    public ArrayList<GameData> getGames() {
        return null;
    }

    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void saveGame(GameData updatedGame) {

    }

    @Override
    public void clear() {

    }
}
