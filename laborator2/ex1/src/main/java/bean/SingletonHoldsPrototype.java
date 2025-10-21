package bean;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SingletonHoldsPrototype {
    // This is resolved once (at context creation), so it won’t change
    private final PrototypeBean injectedOnce;

    // This can produce a fresh PrototypeBean on demand
    private final ObjectFactory<PrototypeBean> prototypeFactory;

    @Autowired
    public SingletonHoldsPrototype(PrototypeBean injectedOnce,
                                   ObjectFactory<PrototypeBean> prototypeFactory) {
        this.injectedOnce = injectedOnce;
        this.prototypeFactory = prototypeFactory;
    }

    public String injectedOnceId() {
        return injectedOnce.id();
    }

    public String freshPrototypeId() {
        return prototypeFactory.getObject().id();
    }
}
