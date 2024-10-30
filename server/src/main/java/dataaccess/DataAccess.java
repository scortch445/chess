package dataaccess;

import model.*;

import java.util.ArrayList;

public interface DataAccess {
    UserData getUser(String username) throws DataAccessException;
    void saveUser(UserData userData) throws DataAccessException;

    AuthData getAuth(String authToken);
    void saveAuth(AuthData authData);
    void deleteAuth(AuthData authData);

    ArrayList<GameData> getGames();
    void createGame(GameData gameData);
    GameData getGame(int gameID);
    void saveGame(GameData updatedGame);

    void clear() throws DataAccessException;
}
