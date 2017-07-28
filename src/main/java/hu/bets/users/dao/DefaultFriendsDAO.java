package hu.bets.users.dao;

import hu.bets.users.model.User;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.List;

public class DefaultFriendsDAO implements FriendsDAO {

    private static final String USER = "User";
    private static final String USER_ID = "userId";
    private static final String NAME = "name";
    private static final String PROFILE_PICTURE = "profilePicture";
    private GraphDatabaseService dbService;

    public DefaultFriendsDAO(GraphDatabaseService dbService) {
        this.dbService = dbService;
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
        Transaction transaction = dbService.beginTx();
        try {

            Node userNode = dbService.createNode(Label.label(USER));
            userNode.setProperty(USER_ID, user.getUserId());
            userNode.setProperty(NAME, user.getName());
            userNode.setProperty(PROFILE_PICTURE, user.getProfilePictureUrl());

            transaction.success();
        } catch (Exception e) {
            transaction.failure();
            throw e;
        }
    }
}
