package hu.bets.users.dao;

import hu.bets.users.model.User;

import java.util.List;

public interface FriendsDAO {

    /**
     * Adds new friends to the user identified by originatingUserId
     *
     * @param originatingUserId - facebook id of the requesting user
     * @param friendsToTrack    - a list of friends to keep in originatingUserId's league
     * @return the number of new friends added. In a perfect senario should be equal to friendsToTrack.size()
     */
    int trackNewFriends(String originatingUserId, List<User> friendsToTrack);

    /**
     * Removes friends from the user's league identified by originatingUserId
     *
     * @param originatingUserId- facebook id of the requesting user
     * @param friendsToUntrack-  a list of friends to delete from originatingUserId's league
     * @return the number of friends removed. In a perfect senario should be equal to friendsToUntrack.size()
     */
    int untrackFriends(String originatingUserId, List<User> friendsToUntrack);

    /**
     * Adds a new user to the database. Will have no friends initially.
     *
     * @param user the user's details
     */
    void registerUser(User user);

    /**
     * Queries the 'TRACKS' relationship to find users assocaited with userId.
     *
     * @param userId - id of the originating user
     * @return list of connections associated to the originating user.
     */
    List<User> getFriends(String userId);
}
