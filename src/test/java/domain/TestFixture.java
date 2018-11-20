package domain;

import org.hsqldb.rights.*;

import java.time.LocalDateTime;

public class TestFixture {

    public static User createDummyUser() {
        return new User.UserBuilder().withUsername("OttoWagner").withVorname("Otto").withLastname("Wagner").withEmail("ottowagner@gmail.com").withCreatedAt(LocalDateTime.now()).withGeschlecht(Geschlecht.MAENNLICH).withLogInStatus(LogInStatus.LOGGED_IN).build();
    }

    public static Artikel createDummyArtikel() {
        Artikel a = Artikel.builder().withName("Apple Macbook").withDescription("Der beste Laptop mit dem besten Design").withHersteller("Apple").withInStock(100).withPreis(1350).withKategorie(Kategorie.COMPUTER).withBildUrl("pics/testbild1.jpg").build();
        return a;
    }

    public static Artikelreservierung createDummyArtikelresevierung(User u,Artikel a) {
        return Artikelreservierung.builder().withUser(u).withArtikel(a).withAbholstatus(Abholstatus.RESERVIERT).withStandort("Spengergasse 21").withReservierungsDatum(LocalDateTime.now()).build();
    }
}
