package mostowska.aleksandra;

import lombok.extern.slf4j.Slf4j;
import mostowska.aleksandra.api.router.SecurityRouter;
import mostowska.aleksandra.api.router.UsersRouter;
import mostowska.aleksandra.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static spark.Spark.*;

@Slf4j // Lombok's annotation to enable logging in this class
public class App {
    public static void main(String[] args) {
        log.info("Setting up routes"); // Log information about starting the route setup

        // Initialize a global exception handler to print error messages to the console
        initExceptionHandler(err -> System.out.println(err.getMessage()));
        port(8080); // Set the server to listen on port 8080

        // Create an application context using the configuration from AppConfig class
        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Retrieve the UsersRouter bean and configure routes for user-related operations
        var usersRouter = context.getBean("usersRouter", UsersRouter.class);
        usersRouter.routes(); // Set up routes for user operations

        // Retrieve the SecurityRouter bean and configure routes related to security
        var securityRouter = context.getBean("securityRouter", SecurityRouter.class);
        securityRouter.routes(); // Set up routes for security operations

        log.info("Routes set up complete"); // Log information about the completion of route setup
    }
}
