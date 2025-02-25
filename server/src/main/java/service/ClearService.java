package service;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
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
    public void clearApplication(ClearAppRequest clearAppRequest){
        userDAO.deleteUserData();
        authDAO.deleteAuthData();
        gameDAO.deleteGames();
    }

}
 