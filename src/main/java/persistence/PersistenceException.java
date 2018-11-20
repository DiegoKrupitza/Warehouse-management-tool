package persistence;

import java.sql.SQLException;

public class PersistenceException extends Exception {

    public PersistenceException(String message) {
        super(message);
    }

    public static PersistenceException forSqlException(SQLException sqlEx) {
        return new PersistenceException(sqlEx.getMessage());
    }

    public static PersistenceException forException(Exception ex) {
        return new PersistenceException(ex.getMessage());
    }
}
