package hu.bets.users.web.api;

import hu.bets.users.service.FriendsService;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/users/football/v1")
public class UsersResource {

    private FriendsService friendsService;

    public UsersResource(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @Path("info")
    @Produces(MediaType.TEXT_HTML)
    public String getInfo() {
        return "<html><body><h1>User service up and running!</h1></body></html>";
    }

    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(String payload) {
        return "";
    }

    @Path("friends")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getFriends(String payload) {
        return "";
    }
}
