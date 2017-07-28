package hu.bets.users.config;

import hu.bets.common.config.model.CommonConfig;
import hu.bets.users.dao.DefaultFriendsDAO;
import hu.bets.users.dao.FriendsDAO;
import hu.bets.users.service.DefaultFriendsService;
import hu.bets.users.service.FriendsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CommonConfig.class)
public class ApplicationConfig {

    @Bean
    public FriendsDAO friendsDAO() {
        return new DefaultFriendsDAO();
    }

    @Bean
    public FriendsService friendsService(FriendsDAO friendsDAO) {
        return new DefaultFriendsService(friendsDAO);
    }
}
