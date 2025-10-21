package bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("prototype")
public class PrototypeBean {
    private final String id = "Prototype-" + UUID.randomUUID();

    public String id() {
        return id;
    }
}