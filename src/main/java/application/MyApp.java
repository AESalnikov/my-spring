package application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.SpringApplication;

/**
 * @author Anton Salnikov
 */
public class MyApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run("application");
        context.close();
    }
}
