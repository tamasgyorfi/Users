package hu.bets.users.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateFriendsPayload {

    private String originatingUserId;
    private List<String> friendsTracked;
    private List<String> friendsUntracked;

    @JsonCreator
    public UpdateFriendsPayload(@JsonProperty("originatingUserId") String originatingUserId,
                                @JsonProperty("friendsTracked") List<String> friendsTracked,
                                @JsonProperty("friendsUntracked") List<String> friendsUntracked) {
        this.originatingUserId = originatingUserId;
        this.friendsTracked = friendsTracked;
        this.friendsUntracked = friendsUntracked;
    }

    public List<String> getFriendsTracked() {
        return Collections.unmodifiableList(friendsTracked);
    }

    public List<String> getFriendsUntracked() {
        return Collections.unmodifiableList(friendsUntracked);
    }

    public String getOriginatingUserId() {
        return originatingUserId;
    }

    @Override
    public String toString() {
        return "UpdateFriendsPayload{" +
                "originatingUserId='" + originatingUserId + '\'' +
                ", friendsTracked=" + friendsTracked +
                ", friendsUntracked=" + friendsUntracked +
                '}';
    }
}
