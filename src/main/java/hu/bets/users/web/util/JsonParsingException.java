package hu.bets.users.web.util;

public class JsonParsingException extends RuntimeException{

    public JsonParsingException(Exception e) {
        super(e);
    }
}
