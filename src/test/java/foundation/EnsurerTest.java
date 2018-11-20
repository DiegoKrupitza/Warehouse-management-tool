package foundation;

import domain.Artikel;
import domain.TestFixture;
import domain.User;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.omg.CORBA.Object;

import static org.junit.Assert.*;


public class EnsurerTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void ensureNotNullStandardFail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("argument must not be null!");

        Object Nuller = null;
        Ensurer.ensureNotNull(Nuller);
    }

    @Test
    public void ensureNotNullStandardGood() throws Exception {
        User NotNuller = TestFixture.createDummyUser();
        Ensurer.ensureNotNull(NotNuller);
    }

    @Test
    public void ensureNotNullGenericFail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Nuller must not be null!");

        Object Nuller = null;
        Ensurer.ensureNotNull(Nuller,"Nuller");
        Assert.fail("No Exception thrown");
    }

    @Test
    public void ensureNotNullGenericGood() throws Exception {

        Artikel NotNuller = TestFixture.createDummyArtikel();
        Ensurer.ensureNotNull(NotNuller,"NotNuller");
    }

    @Test
    public void ensureNotBlankFail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Test-String must not be null, empty or blank!");

        String test = "                          ";
        Ensurer.ensureNotBlank(test,"Test-String");
        Assert.fail("No Excpetion thrown");
    }

    @Test
    public void ensureNotBlankGood() throws Exception {

        String test = "Hallo das ist ein TestString";
        Ensurer.ensureNotBlank(test,"Test-String");
    }

    @Test
    public void isEmail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Email not valid");

        String email = "kru16244spengergasse.at";
        Ensurer.isEmail("email");
        Assert.fail("Failed test, no exception thrown");
    }

    @Test
    public void ensureUserNameLengthGood() throws Exception {
        String username = "Ottowagner";
        Ensurer.ensureUserNameLength(username);
    }

    @Test
    public void ensureUserNameLengthFail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Username musst be longer than 4 Chars");

        String username = "Oti";
        Ensurer.ensureUserNameLength(username);
    }

    @Test
    public void ensureThatNumberIsHigherThanXIntegerGood() throws Exception {
        int higherThan = 5;
        int number = 10;
        Ensurer.ensureThatNumberIsHigherThanX(higherThan,number,"Nummer");
    }


    @Test
    public void ensureThatNumberIsHigherThanXIntegerFail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("2 lower than 5!");

        int higherThan = 5;
        int number = 2;
        Ensurer.ensureThatNumberIsHigherThanX(higherThan,number,"Nummer");
        Assert.fail("No Excpetion thrown");
    }

    @Test
    public void ensureThatNumberIsHigherThanXDoubleGood() throws Exception {
        double higherThan = 5.1;
        double number = 10.1;
        Ensurer.ensureThatNumberIsHigherThanX(higherThan,number,"Nummer");
    }

    @Test
    public void ensureThatNumberIsHigherThanXDoubleFail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("2,100000 lower than 5,100000!");

        double higherThan = 5.1;
        double number = 2.1;
        Ensurer.ensureThatNumberIsHigherThanX(higherThan,number,"Nummer");
        Assert.fail("No Excpetion thrown");
    }

}