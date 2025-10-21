package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"service", "aspect"}) // Scan for components in the specified packages
@EnableAspectJAutoProxy   // Enable the aspect mechanism
public class AppConfig {
}
