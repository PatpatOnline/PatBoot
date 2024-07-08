package cn.edu.buaa.patpat.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class PatBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatBootApplication.class, args);
    }

}
