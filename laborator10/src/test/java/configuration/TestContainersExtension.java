package configuration;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

public class TestContainersExtension implements BeforeAllCallback, AfterAllCallback {

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    static void registerDynamicProperties() {
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        postgreSQLContainer.setPortBindings(List.of("15432:5432"));
        postgreSQLContainer.start();
        registerDynamicProperties();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        postgreSQLContainer.stop();
    }

}
