package howdo.vaccine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.lang.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@SpringBootApplication()
public class Application {

    public static final Logger logger = LogManager.getLogger(Application.class);



    public static void main(String[] args) {

        logger.debug("Initialising Web Server");
        SpringApplication.run(Application.class, args);

    }


}
