package service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("oracle")
public class OracleDbConnection implements DbConnection {

    @Override
    public void connectToDatabase() {
        System.out.println("Connecting to Oracle Database");
    }

}
