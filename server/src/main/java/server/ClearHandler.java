package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.ClearAppRequest;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    ClearService clearService;
    public ClearHandler(ClearService clearService){
        this.clearService = clearService;
    }

    public Object clearApplication(Request req, Response res) throws ResponseException{
        try{
            clearService.clearApplication(new ClearAppRequest());
            res.status(200);
            return "{}";

        } catch (ResponseException e){
            res.status(e.statusCode());
            throw e;
        }
    }
}
