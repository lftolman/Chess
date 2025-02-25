package service;
import dataaccess.*;
import model.*;

import java.util.UUID;

public class UserService {
    private UserDataAccess userDAO;
    private AuthDataAccess authDAO;
    public UserService(UserDataAccess DAO, AuthDataAccess aDAO){
        this.userDAO = DAO;
        this.authDAO = aDAO;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        UserData userData = userDAO.getUser(registerRequest.username());
        if (userData == null){
            UserData newData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.insertUser(newData);
            String newToken = generateToken();
            AuthData newAuth = new AuthData(newToken, registerRequest.username());
            authDAO.createAuth(newAuth);
            return new RegisterResult(registerRequest.username(), newToken);
        }
        else{
            throw new DataAccessException("username already taken");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDAO.getUser(loginRequest.username());
        if (userData != null){
            String newToken = generateToken();
            AuthData newAuth = new AuthData(newToken, loginRequest.username());
            authDAO.createAuth(newAuth);
            return new LoginResult(loginRequest.username(), newToken);
        }
        else{
            throw new DataAccessException("username doesn't exist");
        }

    }

    public void logout(LogoutRequest logoutRequest) {
        AuthData authData = authDAO.getAuth(logoutRequest.authToken());
        authDAO.deleteAuth(authData.authToken());
    }
}