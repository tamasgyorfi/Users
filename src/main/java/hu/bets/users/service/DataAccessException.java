package hu.bets.users.service;

public class DataAccessException extends RuntimeException {

    public DataAccessException(Exception e) {
        super(e);
    }
}
