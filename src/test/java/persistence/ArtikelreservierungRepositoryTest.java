package persistence;

import domain.*;
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

public class ArtikelreservierungRepositoryTest {


    private static Connection connection;

    private Artikelreservierung dummyReservation;
    private User dummyUser;
    private ArtikelreservierungRepository artikelreservierungRepository;

    private ArtikelRepository artikelRepository;
    private Artikel dummyArtikel;
    private UserRepository userRepository;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String TRUNCATE_R_Reservierungen_SQL_STMT = "TRUNCATE TABLE R_Reservierungen";

    private static final String CREATE_STATEMENT_R_Reservierungen_TABLE = "CREATE TABLE R_Reservierungen " +
            "(" +
            "R_ID INTEGER IDENTITY PRIMARY KEY," +
            "R_Version BIGINT," +
            "R_U_ID INTEGER ," +
            "R_A_ID INTEGER ," +
            "R_Standort varchar(255)," +
            "R_Abholstatus varchar(255)," +
            "R_Reservierungsdatum varchar(255)," +
            "R_Abholdatum varchar(255)," +
            ");";


    private static final String TRUNCATE_A_Artikel_SQL_STMT = "TRUNCATE TABLE A_Artikel";

private static final String CREATE_STATEMENT_Artikel_TABLE = "CREATE TABLE IF NOT EXISTS A_Artikel" +
            "(" +
            "A_ID INTEGER IDENTITY PRIMARY KEY," +
            "A_Version BIGINT," +
            "A_Name varchar(255)," +
            "A_Description varchar(255)," +
            "A_Kategorie varchar(255)," +
            "A_Hersteller varchar(255)," +
            "A_InStock BIGINT," +
            "A_Preis DOUBLE," +
            "A_BildUrl varchar(255)," +
            ");";


    private static final String TRUNCATE_U_USER_SQL_STMT = "TRUNCATE TABLE U_User";

    private static final String CREATE_STATEMENT_USER_TABLE = "CREATE TABLE IF NOT EXISTS U_User" +
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

        connection = DriverManager.getConnection("jdbc:hsqldb:mem:testDB", "sa", "");

        connection.setAutoCommit(false);

        Statement stmt = connection.createStatement();

        stmt.execute("Drop TABLE A_Artikel IF EXISTS ");
        stmt.execute("Drop TABLE U_User IF EXISTS ");

        stmt.execute(CREATE_STATEMENT_R_Reservierungen_TABLE);


        stmt.execute(CREATE_STATEMENT_Artikel_TABLE);


        stmt.execute(CREATE_STATEMENT_USER_TABLE);
    }

    @Before
    public void truncateTable() throws Exception {
        Statement statement = connection.createStatement();


        statement.execute(TRUNCATE_R_Reservierungen_SQL_STMT);


        statement.execute(TRUNCATE_A_Artikel_SQL_STMT);


        statement.execute(TRUNCATE_U_USER_SQL_STMT);


        //LOGGER.info(CREATE_STATEMENT_R_Reservierungen_TABLE);
        artikelRepository = new ArtikelRepository();
        artikelreservierungRepository = new ArtikelreservierungRepository();
        userRepository = new UserRepository();

        dummyArtikel = TestFixture.createDummyArtikel();

        dummyArtikel = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        dummyUser = TestFixture.createDummyUser();

        dummyUser = userRepository.registerUser(connection, dummyUser).get();

        dummyReservation = TestFixture.createDummyArtikelresevierung(dummyUser, dummyArtikel);

    }

    @Test
    public void insertTest() throws Exception {
        int davor = artikelreservierungRepository.countTable(connection);

        artikelreservierungRepository.insert(connection, dummyReservation);

        int now = artikelreservierungRepository.countTable(connection);

        Assert.assertEquals(now, davor + 1);

    }

    @Test
    public void insertArtikelReserverungTest() throws Exception {
        Artikelreservierung dummyReservation2 = artikelreservierungRepository.insertArtikelReservation(connection, dummyReservation).get();

        Assert.assertEquals(dummyReservation2.getUser().getUsername(), dummyReservation.getUser().getUsername());

        Assert.assertEquals(dummyReservation2.getStandort(), dummyReservation.getStandort());
    }

    public void findAllTest() throws Exception {
        User u1 = TestFixture.createDummyUser();
        u1.setUsername("Otto1");
        u1 = userRepository.registerUser(connection, u1).get();
        Artikel a1 = TestFixture.createDummyArtikel();
        a1 = artikelRepository.insertNewArtikel(connection, a1);
        Artikelreservierung ar1 = TestFixture.createDummyArtikelresevierung(u1, a1);

        User u2 = TestFixture.createDummyUser();
        u2.setUsername("Maria");
        u2 = userRepository.registerUser(connection, u2).get();
        Artikel a2 = TestFixture.createDummyArtikel();
        a2 = artikelRepository.insertNewArtikel(connection, a2);
        Artikelreservierung ar2 = TestFixture.createDummyArtikelresevierung(u2, a2);

        User u3 = TestFixture.createDummyUser();
        u3.setUsername("Franzi");
        u3 = userRepository.registerUser(connection, u3).get();
        Artikel a3 = TestFixture.createDummyArtikel();
        a3 = artikelRepository.insertNewArtikel(connection, a3);
        Artikelreservierung ar3 = TestFixture.createDummyArtikelresevierung(u3, a3);

        artikelreservierungRepository.insert(connection, ar1);
        artikelreservierungRepository.insert(connection, ar2);
        artikelreservierungRepository.insert(connection, ar3);

        List<Artikelreservierung> artikelreservierungen = artikelreservierungRepository.findAll(connection);

        for (Artikelreservierung artikelreservierung :
                artikelreservierungen) {
            LOGGER.info(artikelreservierung.toString());
        }

    }
@Test
public void findByUserIDTest() throws Exception {
    artikelreservierungRepository.insert(connection,dummyReservation);



    Assert.assertEquals(1,artikelreservierungRepository.findByUserID(connection,dummyUser.getId()).size());

}

    @Test
    public void getTableNameTest() throws Exception {
        Assert.assertSame("R_Reservierungen", artikelreservierungRepository.getTableName());
    }

    @Test
    public void getPrimaryKeyColumnNameTest() throws Exception {
        Assert.assertEquals("R_ID", artikelreservierungRepository.getPrimaryKeyColumnName());
    }
}