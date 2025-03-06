package dataaccess;

import model.AuthData;

public class MySQLAuthDAO implements AuthDataAccess{

    void createAuth(AuthData authData) throws DataAccessException{

    };

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void deleteAuthData() throws DataAccessException;
}
