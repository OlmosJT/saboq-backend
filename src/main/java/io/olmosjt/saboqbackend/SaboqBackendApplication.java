package io.olmosjt.saboqbackend;

import io.olmosjt.saboqbackend.utils.ApplicationUtilsLite;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaboqBackendApplication {

    public static void main(String[] args) {
        var application = new SpringApplication(SaboqBackendApplication.class);
        var env = application.run(args).getEnvironment();
        ApplicationUtilsLite.logApplicationStartup(env);
    }

}
