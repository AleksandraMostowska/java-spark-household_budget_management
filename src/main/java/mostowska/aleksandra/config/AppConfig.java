package mostowska.aleksandra.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.config.adapter.LocalDateTimeAdapter;
import org.jdbi.v3.core.Jdbi;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.config.ConfigLoader;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;

/**
 * Configuration class for setting up application-level beans and settings.
 * This class initializes various components such as Gson, PasswordEncoder,
 * Jdbi for database access, Mailer for email sending, and a SecretKey for JWT.
 */
@Configuration
@ComponentScan("mostowska.aleksandra")
@PropertySource("classpath:/application.properties")
@RequiredArgsConstructor
public class AppConfig {

    private final Environment environment;

    /**
     * Provides a Gson bean for JSON processing.
     *
     * @return A Gson instance configured for pretty printing and custom LocalDateTime handling.
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()  // Configures Gson to format JSON output for readability
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();  // Creates the Gson instance
    }

    /**
     * Provides a PasswordEncoder bean for encoding passwords.
     *
     * @return A PasswordEncoder instance that supports multiple encoding algorithms.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Provides a Jdbi bean for database interactions.
     *
     * @return A Jdbi instance configured with database connection properties.
     */
    @Bean
    public Jdbi jdbi() {
        var jdbi = Jdbi.create(
                environment.getRequiredProperty("db.url"),
                environment.getRequiredProperty("db.username"),
                environment.getRequiredProperty("db.password")
        );

        // TODO: The following code creates database tables, currently commented out
//        var usersTableSql = """
//        create table if not exists users (
//            id integer primary key auto_increment,
//            username varchar(50) not null,
//            email varchar(50) not null,
//            password varchar(255) not null,
//            role varchar(50) not null,
//            budget DECIMAL(10, 2) default 0,
//            budget_after_goals DECIMAL(10, 2) default 0,
//            enabled boolean not null
//        );
//        """;
//        jdbi.useHandle(handle -> handle.execute(usersTableSql));
//
//        var expensesTableSql = """
//        create table if not exists expenses (
//            id integer primary key auto_increment,
//            expense_type varchar(50) not null,
//            custom_expense_type varchar(50),
//            description varchar(255),
//            amount DECIMAL(10, 2) not null,
//            frequency varchar(50) not null,
//            custom_frequency integer,
//            user_id integer not null,
//            foreign key (user_id) references users(id) on delete cascade
//        );
//        """;
//        jdbi.useHandle(handle -> handle.execute(expensesTableSql));
//
//        var incomesTableSql = """
//        create table if not exists incomes (
//            id integer primary key auto_increment,
//            income_type varchar(50) not null,
//            custom_income_type varchar(50),
//            description varchar(255),
//            amount DECIMAL(10, 2) not null,
//            frequency varchar(50) not null,
//            custom_frequency integer,
//            user_id integer not null,
//            foreign key (user_id) references users(id) on delete cascade
//        );
//        """;
//        jdbi.useHandle(handle -> handle.execute(incomesTableSql));
//
//        var investmentsTableSql = """
//        create table if not exists investments (
//            id integer primary key auto_increment,
//            asset_type varchar(50) not null,
//            amount_invested DECIMAL(10, 2) not null,
//            current_value DECIMAL(10, 2),
//            investment_date_time datetime not null,
//            user_id integer not null,
//            foreign key (user_id) references users(id) on delete cascade
//        );
//        """;
//        jdbi.useHandle(handle -> handle.execute(investmentsTableSql));
//
//        var availableInvestmentsTable = """
//        create table if not exists available_investments (
//            id integer primary key auto_increment,
//            asset_type varchar(50) not null,
//            description varchar(255) not null
//        );
//        """;
//        jdbi.useHandle(handle -> handle.execute(availableInvestmentsTable));
//
////        String insertAvailableInvestments = """
////        insert into available_investments (asset_type, description) values (?, ?)
////        """;
////
////        Object[][] investments = {
////                {AssetType.GOLD, "1200; per_ounce; 3% interest"},
////                {AssetType.STOCK, "100; per_share; 5% dividends"},
////                {AssetType.BONDS, "1500; bond; 2% annual_yield"},
////                {AssetType.REAL_ESTATE, "200000; property; 6% rental_yield"},
////                {AssetType.CRYPTOCURRENCY, "50000; per_bitcoin; volatile"},
////                {AssetType.COMMODITIES, "200; per_barrel; fluctuating"},
////                {AssetType.OTHER, "custom; describe; varies"}
////        };
////
////        jdbi.useHandle(handle -> {
////            for (Object[] investment : investments) {
////                handle.createUpdate(insertAvailableInvestments)
////                        .bind(0, investment[0].toString())
////                        .bind(1, investment[1])
////                        .execute();
////            }
////        });
//
//        var goalsTable = """
//            create table if not exists savings_goals (
//                id integer primary key auto_increment,
//                goal_type varchar(50) not null,
//                custom_goal_type varchar(255),
//                percentage DECIMAL(10, 2) not null,
//                user_id integer not null,
//                foreign key (user_id) references users(id) on delete cascade
//            );
//            """;
//        jdbi.useHandle(handle -> handle.execute(goalsTable));

        return jdbi;  // Returns the initialized Jdbi instance
    }

    /**
     * Provides a Mailer bean for sending emails.
     *
     * @return A Mailer instance configured with SMTPS transport strategy for secure email sending.
     */
    @Bean
    public Mailer mailer() {
        ConfigLoader.loadProperties("email-config.properties", true);
        return MailerBuilder
                .withTransportStrategy(TransportStrategy.SMTPS)
                .async()  // Enables asynchronous email sending
                .buildMailer();
    }

    /**
     * Provides a SecretKey bean for JWT signing.
     *
     * @return A SecretKey instance for use with the HS512 signing algorithm.
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

}
