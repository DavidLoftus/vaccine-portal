package howdo.vaccine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication()
public class Application {

    public static final Logger logger = LogManager.getLogger(Application.class);



    public static void main(String[] args) {

        logger.debug("Initialising Web Application");
        SpringApplication.run(Application.class, args);

    }


}
