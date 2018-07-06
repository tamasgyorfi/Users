package hu.bets.users.dao;

import ch.qos.logback.core.encoder.EchoEncoder;
import hu.bets.users.model.User;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultFriendsDAO implements FriendsDAO {

    private static String USER_ID = "userId";
    private static String PROFILE_PICTURE = "profilePicture";
    private static String NAME = "name";

    private static final String IS_USER_REGISTERED_COMMAND = "MATCH (n) WHERE n."+USER_ID+"='%s'  RETURN n";
    private static final String ALL_FRIENDS_COMMAND = "MATCH p=(u1:User { " + USER_ID + ":'%s' })-[:TRACKS]-(u2) RETURN u2." + USER_ID + ", u2." + NAME + ", u2." + PROFILE_PICTURE + "";
    private static final String CREATE_USER_COMMAND = "MERGE (u:User {" + USER_ID + ": '%s', " + NAME + ":'%s', " + PROFILE_PICTURE + ": '%s'})";
    private static final String CREATE_RELATIONSHIP_COMMAND = "MATCH (u1:User {" + USER_ID + ":'%s'}), (u2:User {" + USER_ID + ":'%s'}) CREATE UNIQUE (u1)-[:TRACKS]->(u2)";
    private static final String DELETE_RELATIONSHIP_COMMAND = "MATCH (:User {" + USER_ID + ": '%s'})-[r:TRACKS]-(:User {" + USER_ID + ": '%s'}) DELETE r";

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFriendsDAO.class);

    private Driver driver;

    public DefaultFriendsDAO(Driver driver) {
        this.driver = driver;
    }

    @Override
    public int trackNewFriends(String originatingUserId, List<String> friendsToTrack) {
        return executeBatchCommand(CREATE_RELATIONSHIP_COMMAND, originatingUserId, friendsToTrack);
    }

    @Override
    public int untrackFriends(String originatingUserId, List<String> friendsToUntrack) {
        return executeBatchCommand(DELETE_RELATIONSHIP_COMMAND, originatingUserId, friendsToUntrack);
    }

    private int executeBatchCommand(String command, String originatingUserId, List<String> users) {
        int error = 0;
        for (String user : users) {
            try (Session session = driver.session("register.connection")) {
                session.run(String.format(command, originatingUserId, user));
            } catch (Exception e) {
                LOGGER.error("Unable to execute command {} for users {} and {}.", command, originatingUserId, user);
                error++;
            }
        }

        return users.size() - error;
    }

    @Override
    public void registerUser(User user) {
        try (Session session = driver.session("register.user")) {
            StatementResult result = session.run(String.format(IS_USER_REGISTERED_COMMAND, user.getUserId()));
            if (result.list().isEmpty()) {
                session.run(String.format(CREATE_USER_COMMAND, user.getUserId(), user.getName(), user.getProfilePictureUrl()));
            }
        } catch (Exception e) {
            LOGGER.warn("Unable to register user.", e);
        }
    }

    @Override
    public List<User> getFriends(String userId) {
        List<User> retVal = new ArrayList<>();

        try (Session session = driver.session("query.user.friends")) {
            StatementResult result = session.run(String.format(ALL_FRIENDS_COMMAND, userId));
            List<Record> list = result.list();

            for (Record record : list) {
                String id = "";
                String name = "";
                String profilePic = "";
                for (int i = 0; i < result.keys().size(); i++) {
                    String key = result.keys().get(i);
                    if (key.contains(USER_ID)) {
                        id = record.values().get(i).asString();
                    } else if (key.contains(NAME)) {
                        name = record.values().get(i).asString();
                    } else if (key.contains(PROFILE_PICTURE)) {
                        profilePic = record.values().get(i).asString();
                    }
                }
                retVal.add(new User(id, profilePic, name));
            }
        }

        return retVal;
    }
}
