package hu.bets.users.dao;

import hu.bets.users.model.User;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;

import java.util.List;

public class DefaultFriendsDAO implements FriendsDAO {

    private static final String CREATE_USER_COMMAND = "MERGE (u:%s {userId: '%s', name:'%s', profilePicture: '%s'})";
    private Driver driver;

    public DefaultFriendsDAO(Driver driver) {
        this.driver = driver;
    }

    @Override
    public int trackNewFriends(String originatingUserId, List<User> friendsToTrack) {
        return 0;
    }

    @Override
    public int untrackFriends(String originatingUserId, List<User> friendsToUntrack) {
        return 0;
    }

    @Override
    public void registerUser(User user) {
        try (Session session = driver.session("register.user")) {
            session.run(String.format(CREATE_USER_COMMAND, user.getUserId(), user.getUserId(), user.getName(), user.getProfilePictureUrl()));
        }
    }
}
