package persistence;

import domain.BaseModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Provides the general basic operations one would need for persist and retrieve data
 * from a data store
 *
 * @param <DOMAIN>      the type of the domain class
 * @param <PRIMARY_KEY> the type of the primary key
 */
public interface JdbcRepository<DOMAIN extends BaseModel<DOMAIN, PRIMARY_KEY>,
        PRIMARY_KEY extends Number> {
    /**
     * @param con
     * @param entity
     * @return
     * @throws PersistenceException
     */
    int save(Connection con, DOMAIN entity) throws PersistenceException;

    /**
     * @param con
     * @param id
     * @return
     * @throws PersistenceException
     */
    int delete(Connection con, PRIMARY_KEY id) throws PersistenceException;

    /**
     * Provides convenience method to delete entities by reference
     *
     * @param con
     * @param entity
     * @return
     * @throws PersistenceException
     */
    default int delete(Connection con, DOMAIN entity) throws PersistenceException {
        return (entity.isNew()) ? 1 : delete(con, entity.getId());
    }

    ;

    /**
     * null means nothing found
     *
     * @param con
     * @param id
     * @return
     * @throws PersistenceException
     */
    Optional<DOMAIN> findById(Connection con, PRIMARY_KEY id) throws
            PersistenceException;

    /**
     * @param con
     * @return
     * @throws PersistenceException
     */
    List<DOMAIN> findAll(Connection con) throws PersistenceException;

    /**
     * Stores or gets Statement
     *
     * @param nameOfStatement
     * @param con
     * @param sql
     * @return PrepearedStatement statement
     * @throws SQLException
     */
    default PreparedStatement statementManager(String nameOfStatement, Connection con, String sql) throws SQLException {

        PreparedStatement stmt;

        if (!Statements.hasStmt(nameOfStatement)) {
            stmt = con.prepareStatement(sql);
            Statements.storeStmt(nameOfStatement, stmt);
        }


        stmt = Statements.getStmt(nameOfStatement);

        return stmt;
    }

}