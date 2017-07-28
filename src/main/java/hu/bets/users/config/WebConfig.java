package hu.bets.users.config;

import hu.bets.common.config.CommonWebConfig;
import hu.bets.common.config.model.Resources;
import hu.bets.users.service.FriendsService;
import hu.bets.users.web.api.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CommonWebConfig.class)
public class WebConfig {
    @Bean
    public Resources resources(UsersResource usersResource) {
        return new Resources().addResource(usersResource);
    }

    @Bean
    public UsersResource usersResource(FriendsService friendsService) {
        return new UsersResource(friendsService);
    }
}
