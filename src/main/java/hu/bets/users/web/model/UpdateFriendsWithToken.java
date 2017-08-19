package hu.bets.users.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import hu.bets.users.model.UpdateFriendsPayload;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateFriendsWithToken {

    @JsonUnwrapped
    private final UpdateFriendsPayload updateFriendsPayload;
    private final String token;

    public UpdateFriendsWithToken(UpdateFriendsPayload updateFriendsPayload, String token) {
        this.updateFriendsPayload = updateFriendsPayload;
        this.token = token;
    }

    private UpdateFriendsWithToken() {
        updateFriendsPayload = null;
        token = null;
    }

    public String getToken() {
        return token;
    }

    public UpdateFriendsPayload getUpdateFriendsPayload() {
        return updateFriendsPayload;
    }
}
