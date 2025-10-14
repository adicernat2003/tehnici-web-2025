package service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionService {

    /**
     * @Autowired for DI
     * -on field
     * -on constructor
     * -on setter
     */
//    @Autowired
    private final DbConnection dbConnection;


    private DatabaseConnectionService(@Qualifier("oracle") DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void connect() {
        dbConnection.connectToDatabase();
    }
}
