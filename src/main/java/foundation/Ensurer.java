package foundation;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Ensurer<T> {

    public static <T> T ensureNotNull(T object) {
        return ensureNotNull(object, "argument");
    }

    /**
     * generik ensurerNotNull
     *
     * @param object     which string to check for example DiegoKrupitza
     * @param objectName name of the Object for example Username
     * @return
     */
    public static <T> T ensureNotNull(T object, String objectName) {
        if (object == null) {
            throw new IllegalArgumentException(String.format("%s must not be null!",
                    objectName));
        }
        return object;
    }

    /**
     * @param object     which string to check for example DiegoKrupitza
     * @param objectName name of the Object for example Username
     * @return
     */
    public static String ensureNotBlank(String object, String objectName) {
        if (StringUtils.isBlank(object)) {
            throw new IllegalArgumentException(String.format("%s must not be null, empty or blank!", objectName));
        }
        return object;
    }

    /**
     * @param email
     * @return true if it is an Email
     */
    public static String isEmail(String email) {

        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if (mat.matches()) {

            return email;
        }

        throw new IllegalArgumentException("Email not valid");
    }

    public static String ensureUserNameLength(String username) {
        if (username.length() < 5) {
            throw new IllegalArgumentException("Username musst be longer than 4 Chars");
        }
        return username;
    }

    /**
     *
     * @param highterThan
     * @param checkNumber
     * @param nameOfField
     * @return
     */
    public static Integer ensureThatNumberIsHigherThanX(Integer highterThan, Integer checkNumber, String nameOfField) {

        highterThan = ensureNotNull(highterThan, "Highter Than");
        checkNumber = ensureNotNull(checkNumber, "Check Number");

        if (checkNumber < highterThan) {
            throw new IllegalArgumentException(String.format("%d lower than %d!",
                    checkNumber.intValue(), highterThan.intValue()));
        }

        return checkNumber;
    }

    /**
     *
     * @param highterThan
     * @param checkNumber
     * @param nameOfField
     * @return
     */
    public static Double ensureThatNumberIsHigherThanX(Double highterThan, Double checkNumber, String nameOfField) {

        highterThan = ensureNotNull(highterThan, "Highter Than");
        checkNumber = ensureNotNull(checkNumber, "Check Number");

        if (checkNumber < highterThan) {
            throw new IllegalArgumentException(String.format("%f lower than %f!",
                    checkNumber.doubleValue(), highterThan.doubleValue()));
        }

        return checkNumber;
    }

    public static boolean ensurerIsBlank(String object) {
        if (StringUtils.isBlank(object)) {
            return true;
        }
        return false;
    }

}
