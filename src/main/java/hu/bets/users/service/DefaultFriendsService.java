package hu.bets.users.service;

import hu.bets.users.dao.FriendsDAO;
import hu.bets.users.model.LeagueChangedPayload;
import hu.bets.users.model.LeagueUpdateResult;
import hu.bets.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultFriendsService implements FriendsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFriendsService.class);
    private FriendsDAO friendsDAO;

    public DefaultFriendsService(FriendsDAO friendsDAO) {
        this.friendsDAO = friendsDAO;
    }

    @Override
    public LeagueUpdateResult updateLeague(LeagueChangedPayload payload) {
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

        return null;
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
}
