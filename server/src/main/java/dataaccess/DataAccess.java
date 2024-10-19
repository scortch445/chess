package dataaccess;

import model.*;

public interface DataAccess {
    UserData getUser(String username);
    void saveUser(UserData userData);
    AuthData getAuth(String authToken);
    void saveAuth(AuthData authData);
    void deleteAuth(AuthData authData);
    void clear();
}
