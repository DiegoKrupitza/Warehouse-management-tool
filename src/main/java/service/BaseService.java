package service;

import domain.*;
import org.apache.commons.lang3.StringUtils;
import persistence.ArtikelRepository;
import persistence.ArtikelreservierungRepository;
import persistence.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;

public class BaseService {


    protected static Connection connection;
    protected static ArtikelRepository artikelRepository = new ArtikelRepository();
    protected static ArtikelreservierungRepository artikelreservierungRepository = new ArtikelreservierungRepository();
    protected static UserRepository userRepository = new UserRepository();

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

    private static final String CREATE_STATEMENT_Artikel_TABLE = "CREATE TABLE A_Artikel" +
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


    public static Connection getConnection() throws Exception {
        return (connection != null) ? connection : initDataBase();
    }

    private static Connection initDataBase() throws Exception {

        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        connection = DriverManager.getConnection("jdbc:hsqldb:mem:testDB", "sa", "");

        connection.setAutoCommit(false);

        Statement stmt = connection.createStatement();

        stmt.execute(CREATE_STATEMENT_R_Reservierungen_TABLE);

        stmt.execute(CREATE_STATEMENT_Artikel_TABLE);

        stmt.execute(CREATE_STATEMENT_USER_TABLE);

        initUsers();

        return connection;

    }

    private static void initUsers() throws Exception {

        /*artikelRepository = new ArtikelRepository();
        artikelreservierungRepository = new ArtikelreservierungRepository();
        userRepository = new UserRepository();*/

        Artikel dummyArtikel = createDummyArtikel();


        dummyArtikel = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        User dummyUser = createDummyUser();

        dummyUser = userRepository.registerUser(connection, dummyUser).get();

        userRepository.registerUser(connection, new User.UserBuilder().withUsername("diegokrupitza").withVorname("Diego").withLastname("Krupitza").withEmail("kru16244@spengergasse.at").withCreatedAt(LocalDateTime.now()).withGeschlecht(Geschlecht.MAENNLICH).withLogInStatus(LogInStatus.LOGGED_IN).build());
        userRepository.registerUser(connection, new User.UserBuilder().withUsername("dominikseltenhammer").withVorname("Dominik").withLastname("Seltenhammer").withEmail("sel16333@spengergasse.at").withCreatedAt(LocalDateTime.now()).withGeschlecht(Geschlecht.MAENNLICH).withLogInStatus(LogInStatus.LOGGED_IN).build());

        Artikelreservierung dummyReservation = createDummyArtikelresevierung(dummyUser, dummyArtikel);

        artikelreservierungRepository.insertArtikelReservation(connection, dummyReservation);

    }

    public static User createDummyUser() {
        return new User.UserBuilder().withUsername("OttoWagner").withVorname("Otto").withLastname("Wagner").withEmail("ottowagner@gmail.com").withCreatedAt(LocalDateTime.now()).withGeschlecht(Geschlecht.MAENNLICH).withLogInStatus(LogInStatus.LOGGED_OUT).build();
    }

    public static Artikel createDummyArtikel() {
        Artikel a = Artikel.builder().withName("Apple Macbook").withDescription("Der beste Laptop mit dem besten Design").withHersteller("Apple").withInStock(100).withPreis(1350).withKategorie(Kategorie.COMPUTER).withBildUrl("macbookpro13Articel_0.jpeg    ").build();
        return a;
    }

    public static Artikelreservierung createDummyArtikelresevierung(User u, Artikel a) {
        return Artikelreservierung.builder().withUser(u).withArtikel(a).withAbholstatus(Abholstatus.RESERVIERT).withStandort("Spengergasse 21").withReservierungsDatum(LocalDateTime.now()).withAbholstatus(Abholstatus.RESERVIERT).build();
    }

    public long isURLValidForDelete(HttpServletResponse resp, String requestedUrl) throws IOException {
        if (StringUtils.countMatches(requestedUrl, "/") != 3) {
            //resp.getWriter().print("Fail1");
            return -1;
        }
        String idToDelete = requestedUrl.substring(requestedUrl.lastIndexOf("/") + 1);


        if (!StringUtils.isNumeric(idToDelete)) {
            //resp.getWriter().print("Fail2");
            return -1;
        }
        return Long.parseLong(idToDelete);
    }

    public long isURLValidForEdit(HttpServletResponse resp, String requestedUrl) throws IOException {
        if (StringUtils.countMatches(requestedUrl, "/") != 3) {
            //resp.getWriter().print("Fail1");
            return -1;
        }

        String idToDelete = requestedUrl.substring(requestedUrl.lastIndexOf("/") + 1);

        if (!StringUtils.isNumeric(idToDelete)) {
            //resp.getWriter().print("Fail2");
            return -1;
        }
        return Long.parseLong(idToDelete);
    }


    public long isURLValidForUploadImageArticel(HttpServletResponse resp, String requestedUrl) {
        if (StringUtils.countMatches(requestedUrl, "/") != 3) {
            //resp.getWriter().print("Fail1");
            return -1;
        }

        String idToDelete = requestedUrl.substring(requestedUrl.lastIndexOf("/") + 1);


        if (!StringUtils.isNumeric(idToDelete)) {
            //resp.getWriter().print("Fail2");
            return -1;
        }
        return Long.parseLong(idToDelete);
    }
}
