import chess.*;
import net.REPL;

import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        System.out.println(SET_TEXT_COLOR_PURPLE + "♕ 240 Chess Client " + WHITE_KING);

        new REPL(serverUrl).run();
    }
}


