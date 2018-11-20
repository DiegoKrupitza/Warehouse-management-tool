package domain;

import foundation.Ensurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class User extends BaseModel<User, Long> {

    /**
     * Username of the Person which is also displayed
     */
    private String username;

    /**
     * Vorname of the User
     */
    private String vorname;

    /**
     * Lastname of the User
     */
    private String lastname;

    /**
     * Email for login
     */
    private String email;

    /**
     * Date the User was createdAt
     */
    private LocalDateTime createdAt;

    /**
     * Type of Sex from user
     */
    private Geschlecht geschlecht;

    /**
     * enum if user ist loged in
     */
    private LogInStatus logInStatus;

    /**
     * @param username
     * @param vorname
     * @param lastname
     * @param email
     * @param createdAt
     * @param geschlecht
     */
    private User(String username, String vorname, String lastname, String email, LocalDateTime createdAt, Geschlecht geschlecht, LogInStatus logInStatus) {
        this.username = Ensurer.ensureUserNameLength(Ensurer.ensureNotBlank(username, "Username"));
        this.vorname = Ensurer.ensureNotBlank(vorname, "Vorname");
        this.lastname = Ensurer.ensureNotBlank(lastname, "Lastname");
        this.email = Ensurer.isEmail(email);
        this.createdAt = Ensurer.ensureNotNull(createdAt, "CreatedAt");
        this.geschlecht = geschlecht;
        this.logInStatus = logInStatus;


        initFieldNames();

    }


    public static UserBuilder Builder() {
        return new UserBuilder();
    }

    /**
     * sorts User with Username
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(User o) {
        return o.getUsername().compareTo(this.getUsername());

    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBeautifulDate(LocalDateTime ldt){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = ldt;
        String formattedDateTime = dateTime.format(formatter); // "1986-04-08 12:30"

        return formattedDateTime;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getVorname() {
        return vorname;
    }

    public LogInStatus getLogInStatus() {
        return logInStatus;
    }


    public Geschlecht getGeschlecht() {
        return geschlecht;
    }

    public String getBeautifulSex(Geschlecht geschlecht){
        return geschlecht.name().substring(0,1).toUpperCase()+geschlecht.name().substring(1,geschlecht.name().length()).toLowerCase();
    }

    public void setEmail(String email) {
        this.email = Ensurer.isEmail(email);
    }

    public void setLastname(String lastname) {
        this.lastname = Ensurer.ensureNotBlank(lastname, "Lastname");
    }

    public void setUsername(String username) {
        this.username = Ensurer.ensureUserNameLength(Ensurer.ensureNotBlank(username, "Username"));

    }

    public void setVorname(String vorname) {
        this.vorname = Ensurer.ensureNotBlank(vorname, "Vorname");
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = Ensurer.ensureNotNull(createdAt, "CreatedAt");
    }

    public void setGeschlecht(Geschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    public void setLogInStatus(LogInStatus logInStatus) {
        this.logInStatus = logInStatus;
    }

    /******************************/
    /********** HashMapFielNames **********/
    /****************************/

    public static Map<String, String> fieldNames = null;

    public static Map<String, String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(HashMap<String, String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public void initFieldNames() {
        if (User.fieldNames == null) {
            fieldNames = new HashMap<>();
            fieldNames.put("id", "U_ID");
            fieldNames.put("version", "U_Version");
            fieldNames.put("username", "U_Username");
            fieldNames.put("vorname", "U_Vorname");
            fieldNames.put("lastname", "U_Lastname");
            fieldNames.put("email", "U_Email");
            fieldNames.put("geschlecht", "U_Geschlecht");
            fieldNames.put("createdAt", "U_CreatedAt");
            fieldNames.put("logInStatus", "U_LogInStatus");
        }
    }

    public static String getColumnName(String key) {
        return fieldNames.get(key);
    }

    /******************************/
    /********** builder **********/
    /****************************/

    public static class UserBuilder {

        private String username;
        private String vorname;
        private String lastname;
        private String email;
        private LocalDateTime createdAt;
        private Geschlecht geschlecht;
        private LogInStatus logInStatus;

        public UserBuilder() {

        }

        public UserBuilder withLogInStatus(LogInStatus logInStatus) {
            this.logInStatus = logInStatus;
            return this;
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withVorname(String vorname) {
            this.vorname = vorname;
            return this;
        }

        public UserBuilder withLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserBuilder withGeschlecht(Geschlecht geschlecht) {
            this.geschlecht = geschlecht;
            return this;
        }

        // Construfto erweitern
        public User build() {
            return new User(username, vorname, lastname, email, createdAt, geschlecht, logInStatus);
        }

    }
}
