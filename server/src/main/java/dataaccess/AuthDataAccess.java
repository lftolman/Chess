package dataaccess;

import model.AuthData;

public interface AuthDataAccess{
    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(AuthData authData) throws DataAccessException;

    void deleteAuthData() throws DataAccessException;
}
