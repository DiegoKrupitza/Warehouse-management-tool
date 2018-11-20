package persistence;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public interface JdbcDateTimeConversionSupport {

    default LocalDateTime fromDatabaseValue(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    default Timestamp toDatabaseValue(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}
