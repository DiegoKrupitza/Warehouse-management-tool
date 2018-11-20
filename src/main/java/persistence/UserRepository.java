package persistence;

import domain.Geschlecht;
import domain.LogInStatus;
import domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static foundation.Ensurer.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends AbstractJdbcRepository<User, Long> implements JdbcDateTimeConversionSupport {


    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected PreparedStatement deleteStmt = null;

    @Override
    protected int insert(Connection con, User entity) throws PersistenceException {

        // Check if the entity is new
        if (!entity.isNew()) {
            throw new IllegalStateException("User is already inserted. Update in case of edit"); // is already in database
        }

        // Info from user //

        Integer version = 0;
        String username = entity.getUsername();
        String vorname = entity.getVorname();
        String lastname = entity.getLastname();
        String email = entity.getEmail();
        Timestamp createdTime = toDatabaseValue(entity.getCreatedAt()); // need to convert to Timestamp for databse
        String geschlecht = entity.getGeschlecht().name();
        String logInStatus = entity.getLogInStatus().name();


        try {

            String insertStatement = String.format("INSERT INTO %s " +
                            "(%s,%s,%s,%s,%s,%s,%s,%s)" +
                            "VALUES" +
                            "(?,?,?,?,?,?,?,?)", getTableName(), User.getColumnName("version"), User.getColumnName("username"), User.getColumnName("vorname"),
                    User.getColumnName("lastname"), User.getColumnName("email"), User.getColumnName("createdAt"), User.getColumnName("geschlecht"),
                    User.getColumnName("logInStatus"));

            PreparedStatement inserPreparedStmt = statementManager("Insert_User", con, insertStatement);


            inserPreparedStmt.setInt(1, version.intValue());
            inserPreparedStmt.setString(2, username);
            inserPreparedStmt.setString(3, vorname);
            inserPreparedStmt.setString(4, lastname);
            inserPreparedStmt.setString(5, email);
            inserPreparedStmt.setTimestamp(6, createdTime);
            inserPreparedStmt.setString(7, geschlecht);
            inserPreparedStmt.setString(8,logInStatus);

            return inserPreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    /**
     * This method registers a new User to the Database
     * return User object if everything is ok
     *
     * @param con
     * @param u
     * @return the inserted User
     */
    public Optional<User> registerUser(Connection con, User u) {

        if (countForName(con, u.getUsername()) != 0) {
            return null;
        }

        try {
            insert(con, u);

            Optional<User> usernew = findByUsername(con,u.getUsername());
            return usernew;
        } catch (PersistenceException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * checks if Username already exist if not return true else false
     *
     * @param con
     * @param username
     * @return count of Users with Username
     */
    protected int countForName(Connection con, String username) {


        ensureNotBlank(username, "Check-Username");

        String checkSQL = String.format("SELECT COUNT(*) as Anz FROM %s WHERE %s = ?", getTableName(), User.getFieldNames().get("username"));
        try {
            PreparedStatement stmt = statementManager("CountForName_User", con, checkSQL);
            stmt.setString(1,username);

            int anz_usernames = -1;
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                anz_usernames = res.getInt("Anz");
            }

            return anz_usernames;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public User updateUser(Connection con, User u) throws PersistenceException {
        update(con, u);
        return (findByUsername(con, u.getUsername()).get());
    }


    @Override
    protected int update(Connection con, User entity) throws PersistenceException {
        // Check if the entity is new
        if (entity.isNew()) {
            throw new IllegalStateException("User is not existing, insert a User before!"); // is already in database
        }

        // Info from user //

        Long id = entity.getId();
        Integer version = entity.getVersion() + 1;
        String username = entity.getUsername();
        String vorname = entity.getVorname();
        String lastname = entity.getLastname();
        String email = entity.getEmail();
        Timestamp createdTime = toDatabaseValue(entity.getCreatedAt()); // need to convert to Timestamp for databse
        String geschlecht = entity.getGeschlecht().name();
        String logInStatus = entity.getLogInStatus().name();


        try {

            String updateStatement = String.format("UPDATE %s SET %s = ? , %s = ? , %s= ? , %s= ? , %s= ? , %s= ? , %s= ? , %s= ?"
                            + " WHERE %s = ? AND %s = ?",
                    getTableName(),
                    User.getColumnName("version"), User.getColumnName("username"), User.getColumnName("vorname"),
                    User.getColumnName("lastname"), User.getColumnName("email"), User.getColumnName("createdAt"),
                    User.getColumnName("geschlecht"), User.getColumnName("logInStatus"),
                    getPrimaryKeyColumnName(), User.getColumnName("version"));

            PreparedStatement updatePreparedStmt = statementManager("Update_User", con, updateStatement);


            updatePreparedStmt.setInt(1, version.intValue());
            updatePreparedStmt.setString(2, username);
            updatePreparedStmt.setString(3, vorname);
            updatePreparedStmt.setString(4, lastname);
            updatePreparedStmt.setString(5, email);
            updatePreparedStmt.setTimestamp(6, createdTime);
            updatePreparedStmt.setString(7, geschlecht);
            updatePreparedStmt.setString(8, logInStatus);
            updatePreparedStmt.setLong(9, id);
            updatePreparedStmt.setInt(10, version - 1);


            return updatePreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    protected String getTableName() {
        return "U_User";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "U_ID";
    }

    /**
     * @param con
     * @param id
     * @return Optional with User
     * @throws PersistenceException
     */
    @Override
    public Optional<User> findById(Connection con, Long id) throws PersistenceException {
        try {

            String selectSmt = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), getPrimaryKeyColumnName());
            PreparedStatement stmt = statementManager("findByID_User", con, selectSmt);

            if (id == null) {
                stmt.setNull(1, Types.BIGINT);
            } else {
                stmt.setLong(1, id.longValue());
            }

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                return Optional.of(getUserFromResultSet(result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param con
     * @param username
     * @return Optinal with User
     * @throws PersistenceException
     */
    public Optional<User> findByUsername(Connection con, String username) throws PersistenceException {

        String selectSmt = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), User.getColumnName("username"));

        try {
            PreparedStatement selectAllStmt = statementManager("FindByUsername_User", con, selectSmt);
            if (username == null) {
                selectAllStmt.setNull(1, Types.NVARCHAR);
            } else {
                selectAllStmt.setString(1, username);
            }

            ResultSet result = selectAllStmt.executeQuery();


            while (result.next()) {
                return Optional.of(getUserFromResultSet(result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Optional.empty();
    }

    @Override
    public List<User> findAll(Connection con) throws PersistenceException {

        try {
            String selectStatement = String.format("SELECT * FROM %s", getTableName());

            PreparedStatement selectAllStmt = statementManager("FindAll_User", con, selectStatement);

            ResultSet resultSetDataBase = selectAllStmt.executeQuery();
            // JDBC kann noch nicht Streams :(

            return getAllDataFromResultSet(resultSetDataBase);

        } catch (SQLException sqlEx) {
            throw PersistenceException.forSqlException(sqlEx);
        }


    }

    /**
     * Return all User in a ArrayList from ResultSet
     *
     * @param resultSetDataBase
     * @return Userlist
     * @throws SQLException
     */
    public ArrayList<User> getAllDataFromResultSet(ResultSet resultSetDataBase) throws SQLException {
        ArrayList<User> returnList = new ArrayList<>();
        while (resultSetDataBase.next()) {
            returnList.add(getUserFromResultSet(resultSetDataBase));
        }
        return returnList;
    }


    /**
     * @param resultSetDataBase
     * @return Userobject
     * @throws SQLException
     */
    public User getUserFromResultSet(ResultSet resultSetDataBase) throws SQLException {

        Long U_ID = resultSetDataBase.getLong(User.getColumnName("id"));
        Integer version = resultSetDataBase.getInt(User.getColumnName("version"));
        String username = resultSetDataBase.getString(User.getColumnName("username"));
        String vorname = resultSetDataBase.getString(User.getColumnName("vorname"));
        String lastname = resultSetDataBase.getString(User.getColumnName("lastname"));
        String email = resultSetDataBase.getString(User.getColumnName("email"));
        LocalDateTime createdTime = fromDatabaseValue(resultSetDataBase.getTimestamp(User.getColumnName("createdAt")));
        Geschlecht geschlecht = Geschlecht.valueOf(resultSetDataBase.getString(User.getColumnName("geschlecht")));
        LogInStatus logInStatus = LogInStatus.valueOf(resultSetDataBase.getString(User.getColumnName("logInStatus")));

        User u = new User.UserBuilder().withUsername(username).withLastname(lastname).withVorname(vorname).withEmail(email).withCreatedAt(createdTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();

        // version and id //

        u.setVersion(version);
        u.setId(U_ID);

        return u;

    }
}
