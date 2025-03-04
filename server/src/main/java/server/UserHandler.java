package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import service.UserService;
import spark.*;

public class UserHandler {
    UserService userService;
    public UserHandler(UserService userService){
        this.userService = userService;
    }

    public Object register(Request req, Response res) throws ResponseException {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        try{
            RegisterResult registerResult = userService.register(registerRequest);
            res.status(200);
            return new Gson().toJson(registerResult);
        } catch (ResponseException e){
            res.status(e.statusCode());
            throw e;
        }
    }

    public Object login(Request req, Response res) throws ResponseException {
        LoginRequest loginRequest = new Gson().fromJson(req.body(),LoginRequest.class);
        try{
            LoginResult loginResult = userService.login(loginRequest);
            res.status(200);
            return new Gson().toJson(loginResult);

        } catch (ResponseException e){
            res.status(e.statusCode());
            throw e;
        }
    }

    public Object logout(Request req, Response res) throws ResponseException {
        LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
        try{
            userService.logout(logoutRequest);
            res.status(200);
            return  "{}";

        } catch (ResponseException e){
            res.status(e.statusCode());
            throw e;
        }
    }
}
