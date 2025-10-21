package service;

import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greet(String name) {
        return "Hello, " + name + "!";
    }

    @Override
    public void riskyOperation() {
        if (System.currentTimeMillis() % 2 == 0) {
            throw new IllegalStateException("Boom! Something went wrong.");
        }
    }
}
