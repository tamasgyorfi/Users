package hu.bets.users.model;

import java.util.Collections;
import java.util.List;

public class LeagueChangedPayload {

    private String originatingUserId;
    private List<User> friendsTracked;
    private List<User> friendsUntracked;
    private String token;

    public LeagueChangedPayload(String originatingUserId, List<User> friendsTracked, List<User> friendsUntracked, String token) {
        this.originatingUserId = originatingUserId;
        this.friendsTracked = friendsTracked;
        this.friendsUntracked = friendsUntracked;
        this.token = token;
    }

    public List<User> getFriendsTracked() {
        return Collections.unmodifiableList(friendsTracked);
    }

    public List<User> getFriendsUntracked() {
        return Collections.unmodifiableList(friendsUntracked);
    }

    public String getToken() {
        return token;
    }

    public String getOriginatingUserId() {
        return originatingUserId;
    }

    @Override
    public String toString() {
        return "LeagueChangedPayload{" +
                "originatingUserId='" + originatingUserId + '\'' +
                ", friendsTracked=" + friendsTracked +
                ", friendsUntracked=" + friendsUntracked +
                ", token='" + token + '\'' +
                '}';
    }
}
