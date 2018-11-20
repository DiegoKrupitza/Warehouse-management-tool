package domain;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ArtikelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void dummyArtikelTest() {
        Artikel a1 = TestFixture.createDummyArtikel();
        assertEquals("Apple Macbook", a1.getName());
    }

    @Test
    public void compareToGoodTest() {
        Artikel a1 = TestFixture.createDummyArtikel();
        Artikel a2 = Artikel.builder().withName("Dell Xp1").withDescription("Dünnster Laptop der Welt").withHersteller("Dell").withInStock(50).withPreis(3249.3).withKategorie(Kategorie.COMPUTER).withBildUrl("pics/testbild1.jpg").build();
        Assert.assertNotEquals(0, a1.compareTo(a2));
    }

    @Test
    public void compareToFailTest() {
        Artikel a1 = TestFixture.createDummyArtikel();
        Artikel a2 = TestFixture.createDummyArtikel();
        Assert.assertEquals(0, a1.compareTo(a2));
    }


    /********************/
    /** Builder test ***/
    /******************/

    @Test
    public void builderTestWithName() {
        String name = "Macbook";
        String description = "Description";
        String hersteller = "Apple";
        Integer inStock = 100;
        double preis = 1234.3;
        Kategorie kategorie = Kategorie.COMPUTER;
        String bildUrl = "pics/testbild1.jpg";

        Artikel a = Artikel.builder().withName(name).withDescription(description).withHersteller(hersteller).withInStock(inStock).withPreis(preis).withBildUrl(bildUrl).withKategorie(kategorie).withBildUrl(bildUrl).build();

        Assert.assertEquals(a.getName(),name);

    }

    @Test
    public void builderTestWithDescription() {
        String name = "Macbook";
        String description = "Description";
        String hersteller = "Apple";
        Integer inStock = 100;
        double preis = 1234.3;
        Kategorie kategorie = Kategorie.COMPUTER;
        String bildUrl = "pics/testbild1.jpg";

        Artikel a = Artikel.builder().withName(name).withDescription(description).withHersteller(hersteller).withInStock(inStock).withPreis(preis).withKategorie(kategorie).withBildUrl(bildUrl).build();

        Assert.assertEquals(a.getDescription(),description);

    }

    @Test
    public void builderTestWithHersteller() {
        String name = "Macbook";
        String description = "Description";
        String hersteller = "Apple";
        Integer inStock = 100;
        double preis = 1234.3;
        Kategorie kategorie = Kategorie.COMPUTER;
        String bildUrl = "pics/testbild1.jpg";

        Artikel a = Artikel.builder().withName(name).withDescription(description).withHersteller(hersteller).withInStock(inStock).withPreis(preis).withKategorie(kategorie).withBildUrl(bildUrl).build();

        Assert.assertEquals(a.getHersteller(),hersteller);

    }

    @Test
    public void builderTestWithInStock() {
        String name = "Macbook";
        String description = "Description";
        String hersteller = "Apple";
        Integer inStock = 100;
        double preis = 1234.3;
        Kategorie kategorie = Kategorie.COMPUTER;
        String bildUrl = "pics/testbild1.jpg";

        Artikel a = Artikel.builder().withName(name).withDescription(description).withHersteller(hersteller).withInStock(inStock).withPreis(preis).withKategorie(kategorie).withBildUrl(bildUrl).build();

        Assert.assertEquals(a.getInStock(),inStock);

    }

    @Test
    public void builderTestWithPreis() {
        String name = "Macbook";
        String description = "Description";
        String hersteller = "Apple";
        Integer inStock = 100;
        double preis = 1234.3;
        Kategorie kategorie = Kategorie.COMPUTER;
        String bildUrl = "pics/testbild1.jpg";

        Artikel a = Artikel.builder().withName(name).withDescription(description).withHersteller(hersteller).withInStock(inStock).withPreis(preis).withKategorie(kategorie).withBildUrl(bildUrl).build();

        Assert.assertEquals(a.getPreis() + "",preis + "");

    }



}
