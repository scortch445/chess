package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryDataAccess implements DataAccess {
    private ArrayList<UserData> users = new ArrayList<>();
    private ArrayList<AuthData> authDatas = new ArrayList<>();


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
    public void saveAuth(AuthData authData) {
        authDatas.add(authData);
    }

    @Override
    public void clear() {
        users.clear();
        authDatas.clear();
    }
}
