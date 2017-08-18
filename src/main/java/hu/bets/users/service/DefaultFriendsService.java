package hu.bets.users.service;

import hu.bets.users.dao.FriendsDAO;
import hu.bets.users.model.LeagueChangedPayload;
import hu.bets.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultFriendsService implements FriendsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFriendsService.class);
    private FriendsDAO friendsDAO;

    public DefaultFriendsService(FriendsDAO friendsDAO) {
        this.friendsDAO = friendsDAO;
    }

    @Override
    public List<User> updateLeague(LeagueChangedPayload payload) {
        LOGGER.info("Updating {}'s league with the following payload: {}", payload.getOriginatingUserId(), payload);
        try {
            LOGGER.info("Adding connections: {}", payload.getFriendsTracked());
            int nrOfFriendsTracked = friendsDAO.trackNewFriends(payload.getOriginatingUserId(), payload.getFriendsTracked());
            LOGGER.info("Successfully added {} friends out of {}", nrOfFriendsTracked, payload.getFriendsTracked().size());

            LOGGER.info("Removing connections: {}", payload.getFriendsUntracked());
            int nrOfFriendsUntracked = friendsDAO.untrackFriends(payload.getOriginatingUserId(), payload.getFriendsUntracked());
            LOGGER.info("Successfully added {} friends out of {}", nrOfFriendsUntracked, payload.getFriendsUntracked().size());

            LOGGER.info("Updating {}'s league was successful.", payload.getOriginatingUserId(), payload);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }

        return friendsDAO.getFriends(payload.getOriginatingUserId());
    }

    @Override
    public void register(User user) {
        LOGGER.info("About to register user: {}", user);
        try {
            friendsDAO.registerUser(user);
            LOGGER.info("Successfully registered user: {}", user);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<User> getFriends(String userId) {
        LOGGER.info("About to query friends for user: {}", userId);
        try {
            List<User> friends = friendsDAO.getFriends(userId);
            LOGGER.info("Query run successfully. Result is: {}", friends);

            return friends;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
