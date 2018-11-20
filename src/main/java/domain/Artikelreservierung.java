package domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;


import static foundation.Ensurer.ensureNotBlank;
import static foundation.Ensurer.ensureNotNull;
import static foundation.Ensurer.ensureThatNumberIsHigherThanX;

public class Artikelreservierung extends BaseModel<Artikelreservierung, Long> {

    private Artikel artikel;
    private User user;
    private String standort;
    private Abholstatus abholstatus;
    private LocalDateTime reservierungsDatum;
    private LocalDateTime abholdatum;
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * @param artikel
     * @param user
     * @param standort
     * @param abholstatus
     * @param reservierungsDatum can be null
     * @param abholdatum         can be null
     */
    public Artikelreservierung(Artikel artikel, User user, String standort, Abholstatus abholstatus, LocalDateTime reservierungsDatum, LocalDateTime abholdatum) {
        this.artikel = ensureNotNull(artikel, "Artikel");
        this.user = ensureNotNull(user, "User");
        this.standort = ensureNotBlank(standort, "Standort");
        this.abholstatus = ensureNotNull(abholstatus, "Abholstatus");
        this.reservierungsDatum = reservierungsDatum;
        this.abholdatum = abholdatum;
        initFieldNames();
    }

    public static ArtikelResevierungBuilder builder() {
        return new ArtikelResevierungBuilder();
    }

    public Artikel getArtikel() {
        return artikel;
    }

    public void setArtikel(Artikel artikel) {
        this.artikel = artikel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStandort() {
        return standort;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public Abholstatus getAbholstatus() {
        return abholstatus;
    }

    public void setAbholstatus(Abholstatus abholstatus) {
        this.abholstatus = abholstatus;
    }

    public LocalDateTime getReservierungsDatum() {
        return reservierungsDatum;
    }

    public void setReservierungsDatum(LocalDateTime reservierungsDatum) {
        this.reservierungsDatum = reservierungsDatum;
    }

    public LocalDateTime getAbholdatum() {
        return abholdatum;
    }

    public void setAbholdatum(LocalDateTime abholdatum) {
        this.abholdatum = abholdatum;
    }

    @Override
    public int compareTo(Artikelreservierung o) {
        return o.getId().compareTo(this.getId());
    }

    /******************************/
    /********** HashMapFielNames **********/
    /****************************/

    public static HashMap<String, String> fieldNames = null;

    public static HashMap<String, String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(HashMap<String, String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public void initFieldNames() {
        if (Artikelreservierung.fieldNames == null) {
            fieldNames = new HashMap<>();
            fieldNames.put("id", "R_ID");
            fieldNames.put("version", "R_Version");
            fieldNames.put("user", "R_U_ID");
            fieldNames.put("artikel", "R_A_ID");
            fieldNames.put("standort", "R_Standort");
            fieldNames.put("abholstatus", "R_Abholstatus");
            fieldNames.put("resevierungddatum", "R_Reservierungsdatum");
            fieldNames.put("abholdatum", "R_Abholdatum");
        }
    }

    public static String getColumnName(String key) {
        return fieldNames.get(key);
    }

    /******************************/
    /********** builder **********/
    /****************************/

    public static class ArtikelResevierungBuilder {

        private Artikel artikel;
        private User user;
        private String standort;
        private Abholstatus abholstatus;
        private LocalDateTime reservierungsdatum;
        private LocalDateTime abholdatum;

        public ArtikelResevierungBuilder withArtikel(Artikel artikel) {
            this.artikel = artikel;
            return this;
        }

        public ArtikelResevierungBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public ArtikelResevierungBuilder withStandort(String standort) {
            this.standort = standort;
            return this;
        }

        public ArtikelResevierungBuilder withAbholstatus(Abholstatus abholstatus) {
            this.abholstatus = abholstatus;
            return this;
        }

        public ArtikelResevierungBuilder withReservierungsDatum(LocalDateTime reservierungsDatum) {
            this.reservierungsdatum = reservierungsDatum;
            return this;
        }

        public ArtikelResevierungBuilder withAbholdatum(LocalDateTime abholdatum) {
            this.abholdatum = abholdatum;
            return this;
        }

        public Artikelreservierung build() {
            return new Artikelreservierung(artikel, user, standort, abholstatus, reservierungsdatum, abholdatum);
        }
    }
}
