package service;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import exception.ResponseException;
import model.*;

public class ClearService {
    private GameDataAccess gameDAO;
    private AuthDataAccess authDAO;
    private UserDataAccess userDAO;


    public ClearService(GameDataAccess DAO, AuthDataAccess aDAO, UserDataAccess uDAO){
        this.gameDAO = DAO;
        this.authDAO = aDAO;
        this.userDAO = uDAO;
    }
    public void clearApplication(ClearAppRequest clearAppRequest) throws ResponseException {
        try {
            userDAO.deleteUserData();
            authDAO.deleteAuthData();
            gameDAO.deleteGames();
        } catch (DataAccessException e) {
            throw new ResponseException(500,"Error: %e");
        }
    }

}
 