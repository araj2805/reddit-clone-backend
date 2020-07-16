package org.taker.reddit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;
import org.taker.reddit.config.ApplicationProperties;
import org.taker.reddit.config.Constant;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(ApplicationProperties.class)
public class RedditApplication {

    private static final Logger logger = LoggerFactory.getLogger(RedditApplication.class);

    @Autowired
    private final Environment environment;

    public RedditApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(RedditApplication.class);

        // set default profile
        Map<String, Object> defaultProperties = new HashMap<>();
        defaultProperties.put("spring.profiles.default", Constant.PROFILE_DEVELOPMENT);
        application.setDefaultProperties(defaultProperties);

        Environment environment = application.run(args).getEnvironment();

        logApplicationStartUp(environment);

//        SpringApplication.run(RedditApplication.class, args);
    }

    private static void logApplicationStartUp(Environment environment) {

        String protocol = "http";

        if (environment.getProperty("server.ssl.key-store") != null)
            protocol = "https";

        String serverPort = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");

        if (StringUtils.isEmpty(contextPath))
            contextPath = "/";

        String hostname = "localhost";

        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.warn("The host name could not be determined, using `localhost` as fallback");
        }

        logger.info(
                "\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}" +
                        "\n----------------------------------------------------------",
                environment.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostname,
                serverPort,
                contextPath,
                environment.getActiveProfiles()
        );

        String configServerStatus = environment.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        logger.info(
                "\n----------------------------------------------------------\n\t" +
                        "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus
        );

    }

    @PostConstruct
    public void init() {

        Collection<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

        if (activeProfiles.contains(Constant.PROFILE_DEVELOPMENT) && activeProfiles.contains(Constant.PROFILE_PROD))
            logger.error("You have misconfigured your application! It should not run with both the 'dev' and 'prod' profiles at the same time.");


        if (activeProfiles.contains(Constant.PROFILE_DEVELOPMENT) && activeProfiles.contains(Constant.PROFILE_CLOUD))
            logger.error("You have misconfigured your application! It should not run with both the 'dev' and 'cloud' profiles at the same time.");
    }

}
