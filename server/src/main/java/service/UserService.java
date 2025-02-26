package service;
import dataaccess.*;
import exception.ResponseException;
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

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        try{
            if ((registerRequest.username() == null)||(registerRequest.email()==null)||(registerRequest.password()==null)){
                throw new ResponseException(400,"Error: bad request");
            }
            UserData userData = userDAO.getUser(registerRequest.username());
            if (userData != null){
                throw new ResponseException(403, "Error: already taken");
            }
            UserData newData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.insertUser(newData);
            String newToken = generateToken();
            AuthData newAuth = new AuthData(newToken, registerRequest.username());
            authDAO.createAuth(newAuth);
            return new RegisterResult(registerRequest.username(), newToken);
        } catch (DataAccessException e){
            throw new ResponseException(500,"Error: "+ e.getMessage());
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        try{
            UserData userData = userDAO.getUser(loginRequest.username());
            if (userData == null){
                throw new ResponseException(401,"Error: Username doesn't exist");
            }
            if (!Objects.equals(userData.password(), loginRequest.password())){
                throw new ResponseException(401,"Error: unauthorized");
            }
            String newToken = generateToken();
            AuthData newAuth = new AuthData(newToken, loginRequest.username());
            authDAO.createAuth(newAuth);
            return new LoginResult(loginRequest.username(), newToken);
        } catch (DataAccessException e){
            throw new ResponseException(500,"Error: "+e.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        try{
            AuthData authData = authDAO.getAuth(logoutRequest.authToken());
            if (authData == null){
                throw new ResponseException(401,"Error: unauthorized");
            }
            authDAO.deleteAuth(authData.authToken());
        } catch (DataAccessException e){
            throw new ResponseException(500,"Error: "+e.getMessage());
        }
    }
}