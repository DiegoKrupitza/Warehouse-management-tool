package domain;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class UserTest {

    //TODO alles functioniert test

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void dummyUserTest() {
        User u = TestFixture.createDummyUser();

        assertEquals(u.getVorname(), "Otto");
    }

    @Test
    public void validVornameTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Vorname must not be null, empty or blank!");

        String username = "DiegoKrupitza";
        String vorname = "                          "; // blank
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;

        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();
        Assert.fail("Test Failed, No Exception thrown");
    }

    @Test
    public void validCreateTimeTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("CreatedAt must not be null");

        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = null; // null
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;


        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();
        Assert.fail("Test Failed, No Exception thrown");
    }

    @Test
    public void valitEmailTest() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Email not valid");

        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_OUT;


        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();
        Assert.fail("Test Failed, No Exception thrown");
    }

    @Test
    public void validUsernameFormat() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Username musst be longer than 4 Chars");

        String username = "DK";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;


        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();
        Assert.fail("Test Failed, No Exception thrown");
    }


    /*************************************/
    /*********** Builder test ***********/
    /***********************************/

    @Test
    public void buildUsernameTest() {
        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;

        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();

        Assert.assertEquals(username, u.getUsername());

    }

    @Test
    public void buildVornameTest() {
        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;

        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();

        Assert.assertEquals(vorname, u.getVorname());
    }

    @Test
    public void buildLastnameTest() {
        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;

        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();

        Assert.assertEquals(lastname, u.getLastname());
    }

    @Test
    public void buildEmailTest() {
        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;

        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();

        Assert.assertEquals(createTime, u.getCreatedAt());
    }

    @Test
    public void buildCreateTimeTest() {
        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;

        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();

        Assert.assertEquals(email, u.getEmail());
    }

    @Test
    public void buildGeschlechtTest() {
        String username = "DiegoKrupitza";
        String vorname = "Diego";
        String lastname = "Krupitza";
        String email = "kru16244@spengergasse.at";
        LocalDateTime createTime = LocalDateTime.now();
        Geschlecht geschlecht = Geschlecht.MAENNLICH;
        LogInStatus logInStatus = LogInStatus.LOGGED_IN;

        User u = new User.UserBuilder().withUsername(username).withVorname(vorname).withLastname(lastname).withEmail(email).withCreatedAt(createTime).withGeschlecht(geschlecht).withLogInStatus(logInStatus).build();

        Assert.assertEquals(geschlecht, u.getGeschlecht());
    }


}