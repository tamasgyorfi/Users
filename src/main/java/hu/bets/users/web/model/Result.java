package hu.bets.users.web.model;

public class Result<T> {

    private final T payload;
    private final String error;
    private final String token;

    private Result(T payload, String error, String token) {
        this.payload = payload;
        this.error = error;
        this.token = token;
    }

    public static <T> Result success(T payload, String token) {
        return new Result(payload, "", token);
    }

    public static Result error(String errorMessage, String token) {
        return new Result("", errorMessage, token);
    }
}
