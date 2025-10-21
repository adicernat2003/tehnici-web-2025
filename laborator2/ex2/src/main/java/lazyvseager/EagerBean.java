package lazyvseager;

import org.springframework.stereotype.Component;

@Component
public class EagerBean {

    public EagerBean() {
        System.out.println(">> EagerBean: constructed");
    }

    public void hello() {
        System.out.println("Hello from EagerBean!");
    }
}