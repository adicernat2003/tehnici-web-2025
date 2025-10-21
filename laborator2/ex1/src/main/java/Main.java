import bean.PrototypeBean;
import bean.SingletonBean;
import bean.SingletonHoldsPrototype;
import config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            System.out.println("--- Direct lookups ---");
            var s1 = ctx.getBean(SingletonBean.class);
            var s2 = ctx.getBean(SingletonBean.class);
            System.out.println("Singleton #1: " + s1.id());
            System.out.println("Singleton #2: " + s2.id());
            System.out.println("Same singleton instance? " + (s1 == s2));

            var p1 = ctx.getBean(PrototypeBean.class);
            var p2 = ctx.getBean(PrototypeBean.class);
            System.out.println("Prototype #1: " + p1.id());
            System.out.println("Prototype #2: " + p2.id());
            System.out.println("Same prototype instance? " + (p1 == p2));

            System.out.println("\n--- Prototype inside a Singleton ---");
            var holder = ctx.getBean(SingletonHoldsPrototype.class);
            System.out.println("Injected-once prototype id: " + holder.injectedOnceId());
            System.out.println("Fresh prototype id (call 1): " + holder.freshPrototypeId());
            System.out.println("Fresh prototype id (call 2): " + holder.freshPrototypeId());
        }
    }
}