package net;

import ui.Client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_PURPLE;

public class REPL {
    String serverUrl;
    Client client;


    public REPL(String serverUrl){
        this.serverUrl = serverUrl;
        this.client = new Client(serverUrl, this);
    }

    public void run(){

        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
        out.print(SET_TEXT_COLOR_LIGHT_BLUE + "welcome to 240 chess. type \"help\" to get started.");
        out.println();
        String[] result = getInput(out);
        while (!Objects.equals(result[0], "quit")){
            System.out.print(client.readInput(result));
            result = getInput(out);
        }
        System.out.println("goodbye :)");


    }
    public String[] getInput(PrintStream out){
        if (client.loggedIn){
            out.print(SET_TEXT_COLOR_PURPLE + "\n[LOGGED IN] >>> ");
        }
        else {
            out.print(SET_TEXT_COLOR_PURPLE + "\n[LOGGED OUT] >>> ");
        }
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().split(" ");
    }





}
