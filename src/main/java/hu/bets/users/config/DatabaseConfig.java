package hu.bets.users.config;


import hu.bets.common.util.EnvironmentVarResolver;
import hu.bets.users.dao.DefaultFriendsDAO;
import hu.bets.users.dao.FriendsDAO;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.context.annotation.Bean;

public class DatabaseConfig {

    @Bean
    public Driver neo4jDriver() {
        String graphenedbURL = EnvironmentVarResolver.getEnvVar("GRAPHENEDB_BOLT_URL", ()-> {throw new IllegalArgumentException();});
        String graphenedbUser = EnvironmentVarResolver.getEnvVar("GRAPHENEDB_BOLT_USER", ()-> {throw new IllegalArgumentException();});
        String graphenedbPass = EnvironmentVarResolver.getEnvVar("GRAPHENEDB_BOLT_PASSWORD", ()-> {throw new IllegalArgumentException();});

        return GraphDatabase.driver(graphenedbURL, AuthTokens.basic(graphenedbUser, graphenedbPass));
    }

    @Bean
    public FriendsDAO friendsDAO(Driver driver) {
        return new DefaultFriendsDAO(driver);
    }
}
