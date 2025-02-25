package service;
import dataaccess.*;
import model.*;

import java.util.Objects;
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
        if ((registerRequest.username() == null)||(registerRequest.email()==null)||(registerRequest.password()==null)){
            throw new DataAccessException("Error: bad request");
        }
        UserData userData = userDAO.getUser(registerRequest.username());
        if (userData != null){
            throw new DataAccessException("Error: already taken");
        }
        try{
            UserData newData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.insertUser(newData);
            String newToken = generateToken();
            AuthData newAuth = new AuthData(newToken, registerRequest.username());
            authDAO.createAuth(newAuth);
            return new RegisterResult(registerRequest.username(), newToken);
        } catch (DataAccessException e){
            throw new DataAccessException("Error: %e");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData userData = userDAO.getUser(loginRequest.username());
        if (userData == null){
            throw new DataAccessException("Error: Username doesn't exist");
        }
        if (!Objects.equals(userData.password(), loginRequest.password())){
            throw new DataAccessException("Error: unauthorized");
        }
        try{
            String newToken = generateToken();
            AuthData newAuth = new AuthData(newToken, loginRequest.username());
            authDAO.createAuth(newAuth);
            return new LoginResult(loginRequest.username(), newToken);
        } catch (DataAccessException e){
            throw new DataAccessException("Error: %e");
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData authData = authDAO.getAuth(logoutRequest.authToken());
        if (authData == null){
            throw new DataAccessException("Error: unauthorized");
        }
        try{
            authDAO.deleteAuth(authData.authToken());
        } catch (DataAccessException e){
            throw new DataAccessException("Error: %e");
        }
    }
}