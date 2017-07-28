package hu.bets.users.service;

import hu.bets.users.model.LeagueChangedPayload;
import hu.bets.users.model.LeagueUpdateResult;
import hu.bets.users.model.User;

public interface FriendsService {

    /**
     * Updates a user's league by adding and removing the connections specified in payload.
     *
     * @param payload - payload containing friends to track and untrack
     * @return the result of the update
     */
    LeagueUpdateResult updateLeague(LeagueChangedPayload payload);

    /**
     * Registers a user with no connections iniatially.
     *
     * @param user the user to be added.
     */
    void register(User user);
}
