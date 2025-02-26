package server;

import dataaccess.*;
import exception.ResponseException;
import spark.*;
import service.*;

public class Server {
    static UserService userService;
    private GameService gameService;
    private ClearService clearService;
    private UserHandler userHandler;
    private GameHandler gameHandler;
    private ClearHandler clearHandler;

    public Server(){
        UserDataAccess userDAO = new UserDAO();
        GameDataAccess gameDAO = new GameDAO();
        AuthDataAccess authDAO = new AuthDAO();

        userService = new UserService(userDAO,authDAO);
        gameService = new GameService(gameDAO,authDAO);
        clearService = new ClearService(gameDAO,authDAO,userDAO);

        userHandler = new UserHandler(userService);
        clearHandler = new ClearHandler(clearService);
        gameHandler = new GameHandler(gameService);

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);
        Spark.get("/game", gameHandler::listGames);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);
        Spark.delete("/db",clearHandler::clearApplication);

        Spark.exception(ResponseException.class,this::exceptionHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }
}
