package ui;

import net.Client;
import net.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class ClientCommunicator {
    ServerFacade server;
    Client client;


    public ClientCommunicator(ServerFacade server, Client client){
        this.server = server;
        this.client = client;
    }

    public void run(){
        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
        out.print("Welcome to 240 chess. Type \"help\" to get started");
        out.println();
        String[] result = getInput(out);
        while (!Objects.equals(result[0], "quit")){
            System.out.print(client.readInput(result));
            result = getInput(out);
        }


    }
    public String[] getInput(PrintStream out){
        if (client.loggedIn){
            out.print("\n[LOGGED IN] >>> ");
        }
        else {
            out.print("\n[LOGGED OUT] >>> ");
        }
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }





}
