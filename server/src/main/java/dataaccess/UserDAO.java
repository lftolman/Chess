package dataaccess;

import model.UserData;

import java.util.HashMap;

public class UserDAO implements UserDataAccess{
    HashMap<String,UserData> users = new HashMap<String,UserData>();

    public void insertUser(UserData u){
        String user = u.username();
        users.put(user,u);
    }
    public UserData getUser(String username){
        return users.get(username);
    }
    public void deleteUserData(){
        for (String key :users.keySet()){
            users.remove(key);
        }
    }
}
