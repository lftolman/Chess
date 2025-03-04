package service;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import exception.ResponseException;
import model.*;

public class ClearService {
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;
    private UserDataAccess userDataAccess;


    public ClearService(GameDataAccess DAO, AuthDataAccess aDAO, UserDataAccess uDAO){
        this.gameDataAccess = DAO;
        this.authDataAccess = aDAO;
        this.userDataAccess = uDAO;
    }
    public void clearApplication(ClearAppRequest clearAppRequest) throws ResponseException {
        try {
            userDataAccess.deleteUserData();
            authDataAccess.deleteAuthData();
            gameDataAccess.deleteGames();
        } catch (DataAccessException e) {
            throw new ResponseException(500,"Error: "+ e.getMessage());
        }
    }

}
 