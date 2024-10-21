package dataaccess;

import model.*;

import java.util.ArrayList;

public interface DataAccess {
    UserData getUser(String username);
    void saveUser(UserData userData);

    AuthData getAuth(String authToken);
    void saveAuth(AuthData authData);
    void deleteAuth(AuthData authData);

    ArrayList<GameData> getGames();
    int getNextGameID();

    void clear();
}
