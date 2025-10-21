package bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("singleton") // default, but explicit for clarity
public class SingletonBean {
    private final String id = "Singleton-" + UUID.randomUUID();

    public String id() {
        return id;
    }
}
