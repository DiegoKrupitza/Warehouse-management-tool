package persistence;

import domain.Geschlecht;
import domain.TestFixture;
import domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserRepositoryTest {


    private static Connection connection;
    private UserRepository userRepository;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String TRUNCATE_U_USER_SQL_STMT = "TRUNCATE TABLE U_User";

    private static final String CREATE_STATEMENT_USER_TABLE = "CREATE TABLE U_User" +
            "(" +
            "U_ID INTEGER IDENTITY PRIMARY KEY," +
            "U_Version BIGINT," +
            "U_Username varchar(255)," +
            "U_Vorname varchar(255)," +
            "U_Lastname varchar(255)," +
            "U_Email varchar(255)," +
            "U_Geschlecht varchar(255)," +
            "U_CreatedAt Timestamp," +
            "U_LogInStatus varchar(255) " +
            ");";

    @BeforeClass
    public static void initDataBaseBeforeClass() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        //LOGGER.info("loaded jdbc driver class");

        connection = DriverManager.getConnection("jdbc:hsqldb:mem:testDB", "sa", "");

        connection.setAutoCommit(false);

        //LOGGER.info("setup connection to in-memory DB and configured tx behaviour");

        Statement stmt = connection.createStatement();

        stmt.execute(CREATE_STATEMENT_USER_TABLE);

        //LOGGER.info("created USER table");

    }

    @Before
    public void truncateTable() throws Exception {
        Statement statement = connection.createStatement();
        statement.execute(TRUNCATE_U_USER_SQL_STMT);
    }

    @Before
    public void initVars() {
        userRepository = new UserRepository();
    }


    @Test
    public void insertTest() throws Exception {

        String sql = String.format("SELECT COUNT(*) as Anz FROM %s", userRepository.getTableName());

        PreparedStatement selectDavorStmt = connection.prepareStatement(sql);


        ResultSet resDavor = selectDavorStmt.executeQuery();

        int davor = -1;

        while (resDavor.next()) {
            davor = resDavor.getInt("Anz");
        }

        User u = TestFixture.createDummyUser();
        int result = userRepository.insert(connection, u);
        LOGGER.info("Return from Repository: " + result + "\n");
        PreparedStatement selectDanachStmt = connection.prepareStatement(sql);

        ResultSet resDanach = selectDanachStmt.executeQuery();

        int danach = -1;

        while (resDanach.next()) {
            danach = resDanach.getInt("Anz");
        }

        LOGGER.info("Davor insert: " + davor);
        LOGGER.info("Danach insert: " + danach);

        Assert.assertEquals(davor + 1, danach);

    }

    @Test
    public void registerNewUserTest() throws Exception {
        User dummyUser = TestFixture.createDummyUser();
        User returnUser = userRepository.registerUser(connection, dummyUser).orElseGet(() -> null);

        User real = userRepository.findByUsername(connection, dummyUser.getUsername()).get();

        Assert.assertEquals(returnUser.getUsername(), real.getUsername());
    }

    @Test
    public void countForNameTestGood() {
        // New user1
        User dummyUser1 = TestFixture.createDummyUser();

        // New user2
        User dummyUser2 = TestFixture.createDummyUser();
        dummyUser2.setUsername("OttoWagner2");

        // Add user1 before
        userRepository.registerUser(connection, dummyUser1);

        // check if user exist

        int unique = userRepository.countForName(connection, dummyUser2.getUsername());

        Assert.assertEquals(0, unique);
    }

    @Test
    public void countForNameTestFail() {
        // New user1
        User dummyUser1 = TestFixture.createDummyUser();

        // New user2
        User dummyUser2 = TestFixture.createDummyUser();

        // Add user1 before
        userRepository.registerUser(connection, dummyUser1);

        // check if user exist

        int unique = userRepository.countForName(connection, dummyUser2.getUsername());

        Assert.assertEquals(1, unique);
    }

    @Test
    public void updateTest() throws PersistenceException {
        //User erzeugen und einfügen
        User u = TestFixture.createDummyUser();


        u = userRepository.registerUser(connection, u).orElseGet(() -> null);

        u.setLastname("Krupitza");

        LOGGER.info("");

        User uNew = userRepository.updateUser(connection, u);

        LOGGER.info("Old User:   " + u);
        LOGGER.info("New User:   " + uNew);

        Assert.assertEquals(uNew.getLastname(), "Krupitza");
    }


    @Test
    public void findByIdTest() throws PersistenceException {


        // Insert new User
        User user = TestFixture.createDummyUser();
        int reutrned = userRepository.insert(connection, user);
        // end

        // get ID from user with username from user //
        Long id = userRepository.findByUsername(connection, "OttoWagner").get().getId();
        // end


        Optional<User> userOptional = userRepository.findById(connection, id);

        LOGGER.info(userOptional.toString());


        Assert.assertEquals(user.getVorname(), userOptional.get().getVorname());
    }

    @Test
    public void findByUsername() throws Exception {
        User user = TestFixture.createDummyUser();
        int reutrned = userRepository.insert(connection, user);

        Optional<User> userOptional = userRepository.findByUsername(connection, "OttoWagner");
        Assert.assertEquals(user.getUsername(), userOptional.get().getUsername());
    }

    @Test
    public void findAllTest() throws PersistenceException {

        User u1 = TestFixture.createDummyUser();
        u1.setUsername("Otto1");

        User u2 = TestFixture.createDummyUser();
        u2.setUsername("Marie");


        User u3 = TestFixture.createDummyUser();
        u3.setUsername("Franz");

        userRepository.registerUser(connection, u1);
        userRepository.registerUser(connection, u2);
        userRepository.registerUser(connection, u3);

        List<User> users = userRepository.findAll(connection);

        for (User u :
                users) {
            LOGGER.info(u.toString());
        }

    }

    @Test
    public void getUserFromResultSetTest() throws Exception {
        User user = TestFixture.createDummyUser();
        int enterd = userRepository.insert(connection, user);

        String selectStatement = String.format("SELECT * FROM U_User WHERE U_Username = ?");


        PreparedStatement selectAllStmt = connection.prepareStatement(selectStatement);

        selectAllStmt.setString(1, "OttoWagner");

        // 1 method

        ResultSet resultSetDataBase = selectAllStmt.executeQuery();

        User uVergleichNormal = null;

        while (resultSetDataBase.next()) {

            Long U_ID = resultSetDataBase.getLong("U_ID");
            Integer version = resultSetDataBase.getInt("U_Version");
            String username = resultSetDataBase.getString("U_Username");
            String vorname = resultSetDataBase.getString("U_Vorname");
            String lastname = resultSetDataBase.getString("U_Lastname");
            String email = resultSetDataBase.getString("U_Email");
            LocalDateTime createdTime = resultSetDataBase.getTimestamp("U_CreatedAt").toLocalDateTime();
            Geschlecht geschlecht = Geschlecht.valueOf(resultSetDataBase.getString("U_Geschlecht"));

            uVergleichNormal = new User.UserBuilder().withUsername(username).withLastname(lastname).withVorname(vorname).withEmail(email).withCreatedAt(createdTime).withGeschlecht(geschlecht).build();

            // version and id //

            uVergleichNormal.setVersion(version);
            uVergleichNormal.setId(U_ID);

        }

        // method getAllDataFromResultSet
        resultSetDataBase = selectAllStmt.executeQuery();

        User uVergleichMethode = null;

        while (resultSetDataBase.next()) {
            uVergleichMethode = userRepository.getUserFromResultSet(resultSetDataBase);
        }

        Assert.assertEquals(uVergleichNormal.getId(), uVergleichMethode.getId());

    }

    @Test
    public void getTableNameTest() throws Exception {
        Assert.assertSame("U_User", userRepository.getTableName());
    }

    @Test
    public void getPrimaryKeyColumnNameTest() throws Exception {
        Assert.assertEquals("U_ID", userRepository.getPrimaryKeyColumnName());
    }
}
