package service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("mysql")
public class MySqlDbConnection implements DbConnection {

    @Override
    public void connectToDatabase() {
        System.out.println("Connecting to database with MySql");
    }

}
