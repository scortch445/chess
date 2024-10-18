package dataaccess;

import model.AuthData;
import model.UserData;

public class MemoryDataAccess implements DataAccess {

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void saveUser(UserData userData) {

    }

    @Override
    public void saveAuth(AuthData authData) {

    }
}
