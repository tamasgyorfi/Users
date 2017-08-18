package hu.bets.users.dao;

import hu.bets.users.model.User;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultFriendsDAO implements FriendsDAO {

    private static final String ALL_FRIENDS_COMMAND = "MATCH p=(u1:User { userId:'%s' })-[:TRACKS]-(u2) RETURN DISTINCT u2;";
    private static final String CREATE_USER_COMMAND = "MERGE (u:User {userId: '%s', name:'%s', profilePicture: '%s'})";
    private static final String CREATE_RELATIONSHIP_COMMAND = "MATCH (u1:User {userId:'%s'}), (u2:User {userId:'%s'}) CREATE (u1)-[:TRACKS]->(u2)";
    private static final String DELETE_RELATIONSHIP_COMMAND = "MATCH (:User {userId: '%s'})-[r:TRACKS]-(:User {userId: '%s'}) DELETE r";

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFriendsDAO.class);

    private Driver driver;

    public DefaultFriendsDAO(Driver driver) {
        this.driver = driver;
    }

    @Override
    public int trackNewFriends(String originatingUserId, List<User> friendsToTrack) {
        return executeBatchCommand(DELETE_RELATIONSHIP_COMMAND, originatingUserId, friendsToTrack);
    }

    @Override
    public int untrackFriends(String originatingUserId, List<User> friendsToUntrack) {
        return executeBatchCommand(CREATE_RELATIONSHIP_COMMAND, originatingUserId, friendsToUntrack);
    }

    private int executeBatchCommand(String command, String originatingUserId, List<User> users) {
        int error = 0;
        for (User user : users) {
            try (Session session = driver.session("register.connection")) {
                session.run(String.format(command, originatingUserId, user.getUserId()));
            } catch (Exception e) {
                LOGGER.error("Unable to execute command {} for users {} and {}.", command, originatingUserId, user.getUserId());
                error++;
            }
        }

        return users.size() - error;
    }

    @Override
    public void registerUser(User user) {
        try (Session session = driver.session("register.user")) {
            session.run(String.format(CREATE_USER_COMMAND, user.getUserId(), user.getName(), user.getProfilePictureUrl()));
        }
    }

    @Override
    public List<User> getFriends(String userId) {
        try (Session session = driver.session("query.user.friends")) {
            session.run(String.format(ALL_FRIENDS_COMMAND, userId));
        }

        return null;
    }
}
