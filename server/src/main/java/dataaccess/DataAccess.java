package dataaccess;

import model.*;

import java.util.ArrayList;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException;
    void saveUser(UserData userData) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;
    void saveAuth(AuthData authData) throws DataAccessException;
    void deleteAuth(AuthData authData) throws DataAccessException;

    ArrayList<GameData> getGames() throws DataAccessException;
    void createGame(GameData gameData) throws DataAccessException;
    GameData getGame(int gameID);
    void saveGame(GameData updatedGame);

    void clear() throws DataAccessException;
}
