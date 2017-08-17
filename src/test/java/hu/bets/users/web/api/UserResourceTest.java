package hu.bets.users.web.api;


import hu.bets.servicediscovery.EurekaFacade;
import hu.bets.users.config.ApplicationConfig;
import hu.bets.users.config.DatabaseConfig;
import hu.bets.users.config.WebConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.eclipse.jetty.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class UserResourceTest {
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private ApplicationContext context;

    @Before
    public void before() throws Exception {
        String home = System.getProperty("user.dir");
        URL resource = this.getClass().getClassLoader().getResource(CREDENTIALS_FILE);

        List<String> strings = Files.readAllLines(Paths.get(resource.toURI()));

        strings.forEach(line -> {
            String[] var = line.split("=");
            System.getProperties().setProperty(var[0], var[1]);
        });

        context = new AnnotationConfigApplicationContext(
                FakeApplicationConfig.class,
                DatabaseConfig.class,
                WebConfig.class);

        context.getBean(Server.class).start();

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void registerShouldAddOneUser() throws IOException {

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://" + System.getProperties().getProperty("HOST") +
                ":" + System.getProperties().getProperty("PORT") + "/users/football/v1/register");
        StringEntity entity = new StringEntity("{\"id\": \"id1\" , \"pictureUrl\":\"none\", \"name\":\"name\", \"token\":\"\"}", HTTP.UTF_8);

        entity.setContentType("application/json");
        request.setEntity(entity);

        client.execute(request);

        Driver driver = context.getBean(Driver.class);
        try (Session session = driver.session("read.user")) {
            StatementResult result = session.run("MATCH (n) WHERE n.userId = 'id1' RETURN n.userId, n.profilePicture, n.name");

            List<Value> records = result.list().get(0).values();

            assertEquals("id1", records.get(0).asString());
            assertEquals("none", records.get(1).asString());
            assertEquals("name", records.get(2).asString());

            session.run("MATCH (n) DETACH DELETE n");
        }
    }

    private static class FakeApplicationConfig extends ApplicationConfig {
        @Bean
        @Override
        public EurekaFacade eurekaFacade() {
            return new EurekaFacade() {
                @Override
                public void registerBlockingly(String serviceName) {
                }

                @Override
                public Future<Boolean> registerNonBlockingly(String serviceName) {
                    return null;
                }

                @Override
                public String resolveEndpoint(String name) {
                    return "";
                }

                @Override
                public void unregister() {
                }
            };
        }
    }
}