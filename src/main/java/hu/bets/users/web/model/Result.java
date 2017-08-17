package hu.bets.users.web.model;

public class Result {

    private final String result;
    private final String error;

    private Result(String result, String error) {
        this.result = result;
        this.error = error;
    }

    public static Result success(String message) {
        return new Result(message, "");
    }

    public static Result error(String errorMessage) {
        return new Result("", errorMessage);
    }
}
