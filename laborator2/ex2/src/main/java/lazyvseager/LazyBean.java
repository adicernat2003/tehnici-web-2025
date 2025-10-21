package lazyvseager;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class LazyBean {
    public LazyBean() {
        System.out.println(">> LazyBean: constructed");
    }

    public void hello() {
        System.out.println("Hello from LazyBean!");
    }
}
