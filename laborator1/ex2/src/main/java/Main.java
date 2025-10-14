import config.ContextConfiguration;
import model.BankAccount;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        /**
         * Spring context -> totalitatea componentelor
         * definite pentru framework
         */
        var context = new AnnotationConfigApplicationContext(ContextConfiguration.class);

        /**
         * citire bean din context by name
         * citire bean din context by type
         */
        BankAccount b1 = context.getBean(BankAccount.class);
        BankAccount b2 = context.getBean(BankAccount.class);

//        BankAccount b1 = (BankAccount) context.getBean("bankAcc2");
        System.out.println(b1);
        /**
         * Pentru mai multe beans in context avem mai multe posibilitati de
         * diferentiere:
         * @Primary pe oricare din beanurile pe care le punem in context
         * @Qualifier
         */
    }
}
