package io.olmosjt.saboqbackend.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@UtilityClass
public class ApplicationUtilsLite {

    // ANSI COLORS
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";

    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";

    public static void logApplicationStartup(Environment env) {

        String appName = env.getProperty("spring.application.name", "Application");
        String protocol = isSslEnabled(env) ? "https" : "http";
        String port = env.getProperty("server.port", "8080");
        String contextPath = resolveContextPath(env);
        String host = resolveHostAddress();

        String[] profiles = env.getActiveProfiles().length > 0
                ? env.getActiveProfiles()
                : env.getDefaultProfiles();

        // Swagger
        boolean swaggerEnabled = Boolean.parseBoolean(
                env.getProperty("springdoc.swagger-ui.enabled", "true")
        );

        String swaggerPath = env.getProperty("springdoc.swagger-ui.path", "/swagger-ui.html");
        if (!swaggerPath.startsWith("/")) swaggerPath = "/" + swaggerPath;

        String finalContextPath = contextPath.equals("/") ? "" : contextPath; // root context -> empty
        String swaggerUrl = swaggerEnabled
                ? protocol + "://localhost:" + port + finalContextPath + swaggerPath
                : "Disabled";

        log.info("""

                {}------------------------------------------------------------{}
                {}{} is running!{}
                  {}Profiles:{} {}
                  {}Local URL:{}     {}://localhost:{}{}
                  {}External URL:{}  {}://{}:{}{}
                  {}Swagger:{}       {}
                {}------------------------------------------------------------{}

                """,
                CYAN, RESET,
                BOLD, appName, RESET,
                YELLOW, RESET, String.join(", ", profiles),
                GREEN, RESET, protocol, port, contextPath,
                GREEN, RESET, protocol, host, port, contextPath,
                MAGENTA, RESET, swaggerUrl,
                CYAN, RESET
        );
    }

    private static boolean isSslEnabled(Environment env) {
        return env.containsProperty("server.ssl.key-store") ||
                Boolean.parseBoolean(env.getProperty("server.ssl.enabled", "false"));
    }

    private static String resolveContextPath(Environment env) {
        String cp = env.getProperty("server.servlet.context-path", "/");
        if (cp.isBlank() || "/".equals(cp)) return "/";
        return cp.startsWith("/") ? cp : "/" + cp;
    }

    private static String resolveHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("Unable to determine host address; using localhost.");
            return "localhost";
        }
    }
}