package dataaccess;

import model.UserData;

public class MySQLUserDAO implements UserDataAccess{
    void insertUser(UserData u) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void deleteUserData() throws DataAccessException;
}
