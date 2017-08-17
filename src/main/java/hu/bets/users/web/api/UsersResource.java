package hu.bets.users.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import hu.bets.users.service.DataAccessException;
import hu.bets.users.service.FriendsService;
import hu.bets.users.web.model.Result;
import hu.bets.users.web.model.UserWithToken;
import hu.bets.users.web.util.Json;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/users/football/v1")
public class UsersResource {

    private FriendsService friendsService;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
            return new Json().toJson(Result.success("User successfully registered."));
        } catch (DataAccessException e) {
            return new Json().toJson(Result.error("Unable to register user: " + e.getMessage()));
        }
    }

    @POST
    @Path("/friends")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFriends(String friendsPayload) {
        return "";
    }
}
