package hu.bets.users.web.api;

import hu.bets.users.model.UpdateFriendsPayload;
import hu.bets.users.model.User;
import hu.bets.users.service.DataAccessException;
import hu.bets.users.service.FriendsService;
import hu.bets.users.web.model.Result;
import hu.bets.users.web.model.UpdateFriendsWithToken;
import hu.bets.users.web.model.UserWithToken;
import hu.bets.users.web.util.Json;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/users/football/v1")
public class UsersResource {

    private static final String EMPTY_TOKEN = "empty_token";
    private FriendsService friendsService;

    public UsersResource(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @Path("/info")
    @Produces(MediaType.TEXT_HTML)
    public String getInfo() {
        return "<html><body><h1>User service up and running!</h1></body></html>";
    }

    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(String toRegisterPayload) {
        try {
            UserWithToken userWithToken = new Json().fromJson(toRegisterPayload, UserWithToken.class);
            friendsService.register(userWithToken.getUser());
            return new Json().toJson(Result.success("User successfully registered.", EMPTY_TOKEN));
        } catch (DataAccessException e) {
            return new Json().toJson(Result.error("Unable to register user: " + e.getMessage(), EMPTY_TOKEN));
        }
    }

    @POST
    @Path("/friends")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFriends(String friendsPayload) {
        try {
            UserWithToken userWithToken = new Json().fromJson(friendsPayload, UserWithToken.class);
            List<User> friends = friendsService.getFriends(userWithToken.getUser().getUserId());
            return new Json().toJson(Result.success(friends, EMPTY_TOKEN));
        } catch (DataAccessException e) {
            return new Json().toJson(Result.error("Unable to retrieve friends: " + e.getMessage(), EMPTY_TOKEN));
        }
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateFriends(String updateFriendsPayload) {
        try {
            UpdateFriendsWithToken userWithToken = new Json().fromJson(updateFriendsPayload, UpdateFriendsWithToken.class);
            List<User> friends = friendsService.updateLeague(userWithToken.getUpdateFriendsPayload());
            return new Json().toJson(Result.success(friends, EMPTY_TOKEN));
        } catch (DataAccessException e) {
            return new Json().toJson(Result.error("Unable to update friends: " + e.getMessage(), EMPTY_TOKEN));
        }
    }
}
