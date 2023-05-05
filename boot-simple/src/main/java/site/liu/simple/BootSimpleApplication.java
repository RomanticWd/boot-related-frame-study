package site.liu.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class BootSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootSimpleApplication.class, args);
    }

}
