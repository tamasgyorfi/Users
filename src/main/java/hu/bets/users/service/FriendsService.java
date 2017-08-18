package hu.bets.users.service;

import hu.bets.users.model.LeagueChangedPayload;
import hu.bets.users.model.User;

import java.util.List;

public interface FriendsService {

    /**
     * Updates a user's league by adding and removing the connections specified in payload.
     *
     * @param payload - payload containing friends to track and untrack
     * @return the resulting connections
     */
    List<User> updateLeague(LeagueChangedPayload payload);

    /**
     * Registers a user with no connections iniatially.
     *
     * @param user the user to be added.
     */
    void register(User user);

    /**
     * Retrieves all the friends associated with the user identified by userId.
     *
     * @param userId - id of the originating user
     * @return list of connections associated to the originating user.
     */
    List<User> getFriends(String userId);
}
