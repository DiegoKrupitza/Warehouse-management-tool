package persistence;

import domain.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public abstract class AbstractJdbcRepository<DOMAIN extends BaseModel<DOMAIN,
        PRIMARY_KEY>, PRIMARY_KEY extends Number>
        implements JdbcRepository<DOMAIN, PRIMARY_KEY> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param con
     * @param entity
     * @return
     * @throws PersistenceException
     */
    public final int save(Connection con, DOMAIN entity) throws PersistenceException {
        try {
            if (entity.isNew()) {
                return insert(con, entity);
            } else {
                return update(con, entity);
            }
        } catch (Exception ex) {
            throw PersistenceException.forException(ex);
        }
    }

    /**
     * @param con
     * @param entity
     * @return -1 if fail otherwise the Effectes Rows
     * @throws PersistenceException if user is already added
     */
    protected abstract int insert(Connection con, DOMAIN entity) throws
            PersistenceException;

    /**
     * Return 1 means everything is ok
     * Return -1 means nothing worked
     *
     * @param con
     * @param entity
     * @return
     * @throws PersistenceException
     */
    protected abstract int update(Connection con, DOMAIN entity) throws
            PersistenceException, SQLException;

    /**
     * Return 1 means everything is ok
     * Return -1 means nothing worked
     *
     * @param con
     * @param id
     * @return
     * @throws PersistenceException
     */
    public final int delete(Connection con, PRIMARY_KEY id) throws PersistenceException {

        try {
            String deleteByIdSQL = String.format("DELETE FROM %s WHERE %s = ?", getTableName(), getPrimaryKeyColumnName()); //generate the SQL Statement
            PreparedStatement deleteByIdStmt = statementManager("DeleteStatement", con, deleteByIdSQL);

            // null check is obsolete now
            if (id == null) {
                deleteByIdStmt.setNull(1, Types.BIGINT);

            } else {
                deleteByIdStmt.setLong(1, id.longValue());

            }
            return deleteByIdStmt.executeUpdate();

        } catch (SQLException sqlEx) {

            throw PersistenceException.forSqlException(sqlEx);
        }
    }


    /**
     * @param searchText
     * @param likeType
     * @return
     * @throws Exception
     */
    protected final String likeHandler(String searchText, LikeType likeType) throws Exception {
        if (searchText.contains("%")) {
            throw new Exception("Searchtext already has a Joker in it!");
        }

        if (LikeType.ENDSWITH == likeType) {
            searchText = "%" + searchText;
        } else if (LikeType.STARTSWITH == likeType) {
            searchText = searchText + "%";
        } else if (LikeType.CONTAINS == likeType) {
            searchText = "%" + searchText + "%";
        }

        return searchText;
    }

    /**
     * @param searchText
     * @return
     * @throws Exception
     */
    protected final String startsWithHandler(String searchText) throws Exception {
        return likeHandler(searchText, LikeType.STARTSWITH);
    }

    /**
     * @param searchText
     * @return
     * @throws Exception
     */
    protected final String endsWithHandler(String searchText) throws Exception {
        return likeHandler(searchText, LikeType.ENDSWITH);
    }

    /**
     * @param searchText
     * @return
     * @throws Exception
     */
    protected final String containsHandler(String searchText) throws Exception {
        return likeHandler(searchText, LikeType.CONTAINS);
    }

    /**
     * @param con
     * @return
     * @throws SQLException
     */
    public int countTable(Connection con) throws SQLException {

        String sql = String.format("SELECT COUNT(*) as Anz FROM %s", getTableName());
        PreparedStatement stmt = statementManager("Count_" + getTableName(), con, sql);


        ResultSet res = stmt.executeQuery();

        int davor = -1;

        while (res.next()) {
            davor = res.getInt("Anz");
        }

        return davor;
    }

    /**
     * @return the tablename
     */
    protected abstract String getTableName();

    protected abstract String getPrimaryKeyColumnName();

    public static enum LikeType {
        STARTSWITH, /* % is after the text */
        ENDSWITH, /* % is before the text */
        CONTAINS /* % is around the text */
    }

}
