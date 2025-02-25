package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDAO implements AuthDataAccess{
    HashMap<String, AuthData> auths = new HashMap<String,AuthData>();

    public void createAuth(AuthData authData){
        auths.put(authData.authToken(),authData);
    }

    public AuthData getAuth(String authToken){
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken){
        auths.remove(authToken);
    }

    public void deleteAuthData(){
        for (String key :auths.keySet()){
            auths.remove(key);
        }
    }
}
