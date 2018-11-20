package persistence;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

public class JdbcDateTimeConversionSupportTest {

    private JdbcDateTimeConversionSupport converter = new JdbcDateTimeConversionSupport() {

    };


    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private LocalDateTime compareTimeLocal;

    private Timestamp compareTimeSQL;

    @Before
    public void init() {

        compareTimeSQL = Timestamp.valueOf("2014-01-10 10:10:10");
        compareTimeLocal = LocalDateTime.of(2014, Month.JANUARY, 10, 10, 10,10);

        logger.info("LocalDateTime: " + compareTimeLocal);
        logger.info("Timestamp: " + compareTimeSQL);
    }

    @Test
    public void fromDatabaseValueTest() {
        LocalDateTime result = converter.fromDatabaseValue(compareTimeSQL);
        Assert.assertEquals(compareTimeLocal,result);
    }

    @Test
    public void toDatabaseValueTest() {
        Timestamp result = converter.toDatabaseValue(compareTimeLocal);
        Assert.assertEquals(compareTimeSQL,result);
    }



}
