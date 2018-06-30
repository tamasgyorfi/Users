package hu.bets.users.web.api;

import hu.bets.common.util.json.Json;
import hu.bets.users.model.User;
import hu.bets.users.service.DataAccessException;
import hu.bets.users.service.FriendsService;
import hu.bets.users.web.model.Result;
import hu.bets.users.web.model.UpdateFriendsWithToken;
import hu.bets.users.web.model.UserWithToken;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response register(String toRegisterPayload) {
        try {
            UserWithToken userWithToken = new Json().fromJson(toRegisterPayload, UserWithToken.class);
            friendsService.register(userWithToken.getUser());
            return Response.ok(new Json().toJson(Result.success("User successfully registered.", EMPTY_TOKEN))).build();
        } catch (DataAccessException e) {
            return Response.serverError()
                    .entity(new Json().toJson(Result.error("Unable to register user: " + e.getMessage(), EMPTY_TOKEN)))
                    .build();
        }
    }

    @POST
    @Path("/friends/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFriends(@PathParam("userId") String userId, String friendsPayload) {
        try {
            List<User> friends = friendsService.getFriends(userId);
            return Response.ok(new Json().toJson(Result.success(friends, EMPTY_TOKEN))).build();
        } catch (DataAccessException e) {
            return Response.serverError().entity(new Json().toJson(Result.error("Unable to retrieve friends: " + e.getMessage(), EMPTY_TOKEN))).build();
        }
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFriends(String updateFriendsPayload) {
        try {
            UpdateFriendsWithToken userWithToken = new Json().fromJson(updateFriendsPayload, UpdateFriendsWithToken.class);
            List<User> friends = friendsService.updateLeague(userWithToken.getUpdateFriendsPayload());
            return Response.ok(new Json().toJson(Result.success(friends, EMPTY_TOKEN))).build();
        } catch (DataAccessException e) {
            return Response.serverError()
                .entity(new Json().toJson(Result.error("Unable to update friends: " + e.getMessage(), EMPTY_TOKEN)))
                .build();
        }
    }
}
