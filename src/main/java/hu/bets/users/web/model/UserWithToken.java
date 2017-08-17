package hu.bets.users.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import hu.bets.users.model.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserWithToken {

    @JsonUnwrapped
    private final User user;
    private final String token;

    private UserWithToken() {
        user = null;
        token = null;
    }

    public UserWithToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
