package hu.bets.users;

import com.netflix.config.ConfigurationManager;
import hu.bets.servicediscovery.EurekaFacade;
import hu.bets.services.Services;
import hu.bets.users.config.ApplicationConfig;
import hu.bets.users.config.DatabaseConfig;
import hu.bets.users.config.WebConfig;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

    private final ApplicationContext context = new AnnotationConfigApplicationContext(
            ApplicationConfig.class,
            DatabaseConfig.class,
            WebConfig.class);

    public static void main(String[] args) {
        Starter starter = new Starter();

        starter.registerForDiscovery(starter.context.getBean(EurekaFacade.class));
        addShutDownHook(starter.context);

        starter.setCommandTimeout();
        starter.startServer(starter.context.getBean(Server.class));
    }

    private void setCommandTimeout() {
        ConfigurationManager.getConfigInstance().setProperty(
                "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds",
                10000);
    }

    private static void addShutDownHook(ApplicationContext context) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> context.getBean(EurekaFacade.class).unregister()));
    }

    private void registerForDiscovery(EurekaFacade eurekaFacade) {
        eurekaFacade.registerNonBlockingly(Services.USERS.getServiceName());
    }

    private void startServer(Server server) {
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            LOGGER.error("Unable to start the embedded server.", e);
        }
    }
}
