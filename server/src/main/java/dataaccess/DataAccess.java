package dataaccess;

import model.*;

public interface DataAccess {
    UserData getUser(String username);
    void saveUser(UserData userData);
    void saveAuth(AuthData authData);
}
