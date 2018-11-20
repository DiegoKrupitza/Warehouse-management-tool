package persistence;

import domain.Artikel;
import domain.Kategorie;
import domain.TestFixture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

public class ArtikelRepositoryTest {


    private static Connection connection;
    private ArtikelRepository artikelRepository;
    private Artikel dummyArtikel;

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String TRUNCATE_A_Artikel_SQL_STMT = "TRUNCATE TABLE A_Artikel";

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

    @BeforeClass
    public static void initDataBaseBeforeClass() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");

        connection = DriverManager.getConnection("jdbc:hsqldb:mem:testDB", "sa", "");

        connection.setAutoCommit(false);

        Statement stmt = connection.createStatement();

//        stmt.execute("Drop A_Artikel IF EXISTS");

        stmt.execute(CREATE_STATEMENT_Artikel_TABLE);
    }

    @Before
    public void truncateTable() throws Exception {
        Statement statement = connection.createStatement();
        statement.execute(TRUNCATE_A_Artikel_SQL_STMT);

        //LOGGER.info(CREATE_STATEMENT_Artikel_TABLE);
        artikelRepository = new ArtikelRepository();
        dummyArtikel = TestFixture.createDummyArtikel();

    }

    @Test
    public void insertTest() throws Exception {

        int davor = artikelRepository.countTable(connection);

        LOGGER.info("Davor: " + davor);

        artikelRepository.insert(connection, dummyArtikel);

        int danach = artikelRepository.countTable(connection);

        Assert.assertEquals(davor + 1, danach);
    }

    @Test
    public void countTest() throws Exception {

        int count = artikelRepository.countTable(connection);
        Assert.assertEquals(0, count);
    }

    @Test
    public void countForNameTest() throws Exception {

        artikelRepository.insert(connection, dummyArtikel);

        int counter = artikelRepository.countForName(connection, dummyArtikel.getName());

        Assert.assertEquals(1, counter);
    }

    @Test
    public void countTableTest() throws Exception {
        int davor = artikelRepository.countTable(connection);
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        int danach = artikelRepository.countTable(connection);
        Assert.assertEquals(danach, davor + 1);
    }

    @Test
    public void insertNewArtikelTest() throws Exception {
        int davor = artikelRepository.countTable(connection);
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        int danach = artikelRepository.countTable(connection);

        Assert.assertEquals(a.getName(), dummyArtikel.getName());
        Assert.assertEquals(danach, davor + 1);
    }

    @Test
    public void findByNameTest() throws Exception {
        // TODO
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Assert.assertEquals(a.getName(), dummyArtikel.getName());

    }

    @Test
    public void findByKategorieTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        Artikel result = artikelRepository.findByKategorie(connection, Kategorie.COMPUTER).get(0);

        Assert.assertEquals(result.getName(), a.getName());


    }

    @Test
    public void findByIdTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);


        LOGGER.info("" + artikelRepository.countTable(connection));


        Artikel result = artikelRepository.findById(connection, artikelRepository.findByName(connection, a.getName()).getId()).get();

        Assert.assertEquals(result.getName(), a.getName());

    }

    @Test

    public void updateTest() throws Exception {
        // Artikel erzeugen und einfügen
        Artikel a = TestFixture.createDummyArtikel();
        a = artikelRepository.insertNewArtikel(connection, a);

        a.setName("Lenovo E580");

        Artikel artikel = artikelRepository.updateArtikel(connection, a);

        Assert.assertEquals(artikel.getName(), "Lenovo E580");


    }

    public void likeByNameTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);


        Artikel a2 = dummyArtikel;

        a2 = artikelRepository.insertNewArtikel(connection, a2);

        List<Artikel> artDB = artikelRepository.likeByName(connection, a.getName());
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void likeByDescriptionTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.likeByDescription(connection, a.getDescription());
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void likeByHerstellerTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        LOGGER.info(a.toString());
        LOGGER.info(a2.toString());

        List<Artikel> artDB = artikelRepository.likeByHersteller(connection, a.getHersteller());
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void likeByKategorieTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.likeByKategorie(connection, a.getKategorie().name());
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void nameStartWithTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.nameStartsWith(connection, "Ap");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void nameContainsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.nameContains(connection, "Ap");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void nameEndsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.nameEndsWith(connection, "k");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void descriptionStartWithTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.descriptionStartsWith(connection, "D");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void descriptionContainsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.descriptionContains(connection, "Laptop");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void descriptionEndsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.descriptionEndsWith(connection, "n");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void herstellerStartWithTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.herstellerStartsWith(connection, "A");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void herstellerContainsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.herstellerContains(connection, "pp");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void herstellerEndsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.herstellerEndsWith(connection, "e");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void kategorieStartWithTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.kategorieStartsWith(connection, "C");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void kategorieContainsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.kategorieContains(connection, "E");
        Assert.assertEquals(artDB.size(), 2);
    }

    @Test
    public void kategorieEndsTest() throws Exception {
        Artikel a = artikelRepository.insertNewArtikel(connection, dummyArtikel);
        Artikel a2 = artikelRepository.insertNewArtikel(connection, dummyArtikel);

        List<Artikel> artDB = artikelRepository.kategorieEndsWith(connection, "R");
        Assert.assertEquals(artDB.size(), 2);
    }

}