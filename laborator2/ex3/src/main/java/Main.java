import config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.GreetingService;

public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            GreetingService service = ctx.getBean(GreetingService.class);

            System.out.println("Service call #1:");
            String msg = service.greet("Adrian");
            System.out.println(" -> " + msg);

            System.out.println("\nService call #2 (may throw):");
            try {
                service.riskyOperation();
                System.out.println(" -> riskyOperation completed");
            } catch (Exception e) {
                System.out.println(" -> Caught in main: " + e.getMessage());
            }
        }
    }
}