package ui;

import static ui.EscapeSequences.*;

public class Client {
    public boolean loggedIn = false;

    public String readInput(String[] result){
            switch (result[0]){
                case "help" -> {return help();}
                case "register" -> {return register(result);}
                case "login" -> {return login(result);}
                case "create" -> {return create(result);}
                case "join" -> {return join(result);}
                case "quit" -> {return null;}
                default -> { return "Input not recognized, try again.";}
            }
    }

    public String help(){
        String output = "";
        output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nhelp "+SET_TEXT_COLOR_PURPLE + "- with possible commands";
        if (!loggedIn){
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nregister <USERNAME> <PASSWORD> <EMAIL> " +
                    SET_TEXT_COLOR_PURPLE + "- to create an account";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nlogin <USERNAME> <PASSWORD> " +SET_TEXT_COLOR_PURPLE +
                    "- to play chess";}
        else{
            output = output+SET_TEXT_COLOR_LIGHT_BLUE+"\ncreate <NAME> "+SET_TEXT_COLOR_PURPLE+
                    "- with possible commands";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\njoin <ID> [WHITE|BLACK] "
                    +SET_TEXT_COLOR_PURPLE + "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nobserve <ID> "+SET_TEXT_COLOR_PURPLE + "- a game";
            output = output + SET_TEXT_COLOR_LIGHT_BLUE+"\nlogout "+SET_TEXT_COLOR_PURPLE + "- when you are done";
        }
        output = output + SET_TEXT_COLOR_LIGHT_BLUE+ "\nquit " +SET_TEXT_COLOR_PURPLE + "- playing chess";
        return output;
    }

    public String register(String[] result){
        return null;
    }

    public String login(String[] result){
        this.loggedIn = true;
        return null;
    }

    public String create(String[] result){
        return null;
    }

    public String join(String[] result){
        return null;
    }

    public String observer(String[] result){
        return null;
    }

    public String logout(){
        this.loggedIn = false;
        return null;
    }





}
