package hu.bets.users.web.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bets.servicediscovery.EurekaFacade;
import hu.bets.users.config.ApplicationConfig;
import hu.bets.users.config.DatabaseConfig;
import hu.bets.users.config.WebConfig;
import hu.bets.users.model.UpdateFriendsPayload;
import hu.bets.users.web.model.UpdateFriendsWithToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.BeforeClass;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class UserResourceTest {
    private static final String CREDENTIALS_FILE = "credentials.txt";
    private static ApplicationContext context;

    @BeforeClass
    public static void before() throws Exception {
        String home = System.getProperty("user.dir");
        URL resource = UserResourceTest.class.getClassLoader().getResource(CREDENTIALS_FILE);

        if (resource != null) {
            List<String> strings = Files.readAllLines(Paths.get(resource.toURI()));

            strings.forEach(line -> {
                String[] var = line.split("=");
                System.getProperties().setProperty(var[0], var[1]);
            });
        }
        context = new AnnotationConfigApplicationContext(
                FakeApplicationConfig.class,
                DatabaseConfig.class,
                WebConfig.class);

        context.getBean(Server.class).start();

        TimeUnit.SECONDS.sleep(2);
    }

    @After
    public void tearDown() {
        Driver driver = context.getBean(Driver.class);
        try (Session session = driver.session("delete.users")) {
            session.run("MATCH (n) DETACH DELETE n");
        }
    }

    @Test
    public void registerShouldAddOneUser() throws IOException {

        runPost("/users/football/v1/register", "{\"id\": \"id1\" , \"pictureUrl\":\"none\", \"name\":\"name\", \"token\":\"\"}");

        Driver driver = context.getBean(Driver.class);
        try (Session session = driver.session("read.user")) {
            StatementResult result = session.run("MATCH (n) WHERE n.userId = 'id1' RETURN n.userId, n.profilePicture, n.name");

            List<Value> records = result.list().get(0).values();

            assertEquals("id1", records.get(0).asString());
            assertEquals("none", records.get(1).asString());
            assertEquals("name", records.get(2).asString());
        }
    }

    @Test
    public void shouldReturnAllFriendsForAUser() throws IOException {

        createUsers();
        String result = runPost("/users/football/v1/friends/101", "{\"id\": \"101\" , \"token\":\"\"}");

        assertEquals("{\"payload\":[{\"id\":\"303\",\"pictureUrl\":\"mmmm\",\"name\":\"Bill\"},{\"id\":\"202\",\"pictureUrl\":\"nnnn\",\"name\":\"Jack\"}],\"error\":\"\",\"token\":\"empty_token\"}", result);
    }

    @Test
    public void shouldUpdateFriendsForAUser() throws IOException {

        createUsers();
        UpdateFriendsPayload payload = new UpdateFriendsPayload("101", Arrays.asList("404"), Arrays.asList("202"));
        String stringPayload = new ObjectMapper().writeValueAsString(new UpdateFriendsWithToken(payload, "empty_token"));

        String result = runPost("/users/football/v1/update", stringPayload);

        assertEquals("{\"payload\":[{\"id\":\"404\",\"pictureUrl\":\"llll\",\"name\":\"Ronn\"},{\"id\":\"303\",\"pictureUrl\":\"mmmm\",\"name\":\"Bill\"}],\"error\":\"\",\"token\":\"empty_token\"}", result);
    }

    private String runPost(String path, String payload) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://" + System.getProperties().getProperty("HOST") +
                ":" + System.getProperties().getProperty("PORT") + path);
        StringEntity entity = new StringEntity(payload);

        entity.setContentType("application/json");
        request.setEntity(entity);

        CloseableHttpResponse result = client.execute(request);

        return EntityUtils.toString(result.getEntity());
    }

    private void createUsers() {
        Driver driver = context.getBean(Driver.class);
        try (Session session = driver.session("read.user")) {
            session.run("MERGE (u:User {userId: '101', name:'John', profilePicture: 'none'})");
            session.run("MERGE (u:User {userId: '202', name:'Jack', profilePicture: 'nnnn'})");
            session.run("MERGE (u:User {userId: '303', name:'Bill', profilePicture: 'mmmm'})");
            session.run("MERGE (u:User {userId: '404', name:'Ronn', profilePicture: 'llll'})");

            session.run("MATCH (u1:User {userId:'101'}), (u2:User {userId:'202'}) CREATE (u1)-[:TRACKS]->(u2)");
            session.run("MATCH (u1:User {userId:'101'}), (u2:User {userId:'303'}) CREATE (u1)-[:TRACKS]->(u2)");
            session.run("MATCH (u1:User {userId:'303'}), (u2:User {userId:'404'}) CREATE (u1)-[:TRACKS]->(u2)");
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