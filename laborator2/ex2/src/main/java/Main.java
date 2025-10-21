import config.AppConfig;
import lazyvseager.EagerBean;
import lazyvseager.LazyBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Starting Spring Context ===");
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println("=== Spring Context Initialized ===");

            System.out.println("\nRequesting LazyBean...");
            LazyBean lazy = ctx.getBean(LazyBean.class);
            lazy.hello();

            System.out.println("\nRequesting EagerBean...");
            EagerBean eager = ctx.getBean(EagerBean.class);
            eager.hello();
        }
    }
}