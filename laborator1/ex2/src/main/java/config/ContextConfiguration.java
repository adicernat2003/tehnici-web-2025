package config;

import model.BankAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ContextConfiguration {

    @Bean
    @Primary
    public BankAccount bankAccount1() {
        return new BankAccount(1234, "0005C", "Alex");
    }

    @Bean("bankAcc2")
    public BankAccount bankAccount2() {
        return new BankAccount(1234, "0005B", "Ionut");
    }
}
