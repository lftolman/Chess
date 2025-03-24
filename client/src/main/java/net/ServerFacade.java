package net;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url){
        this.serverUrl = url;
    }


    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        LoginResult result = this.makeRequest("POST", path, request, LoginResult.class);
        this.authToken = result.authToken();
        return result;
    }

    public void logout() throws ResponseException {
        LogoutRequest request = new LogoutRequest(authToken);
        var path = "/session";
        this.makeRequest("DELETE", path, request, null);
        authToken = null;

    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);

    }

    public void join(JoinGameRequest request) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, request, null);

    }

    public ListGamesResult listGames(ListGamesRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, request, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public void clearApp(ClearAppRequest request) throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, request, null);
    }





    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }


    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}


