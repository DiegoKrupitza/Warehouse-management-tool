package domain;


import java.time.LocalDateTime;
import java.util.HashMap;

import static foundation.Ensurer.ensureNotBlank;
import static foundation.Ensurer.ensureNotNull;
import static foundation.Ensurer.ensureThatNumberIsHigherThanX;

public class Artikel extends BaseModel<Artikel, Long> {

    /**
     * Name of the prduct
     */
    public String name;

    /**
     * Description of the product
     */
    public String description;

    /**
     * Name of the productor
     */
    public String hersteller;

    /**
     * Number of Articel in Stock
     */
    public Integer inStock;

    /**
     * Price of one unit
     */
    public double preis;

    /**
     * Kategorie of the Item
     */
    private Kategorie kategorie;

    /**
     * Url für das Bild
     */
    public String bildUrl;


    /**
     * @param name
     * @param description
     * @param hersteller
     * @param inStock
     * @param preis
     * @param bildUrl
     */
    private Artikel(String name, String description, String hersteller, Integer inStock, double preis, Kategorie kategorie, String bildUrl) {
        this.name = ensureNotBlank(name, "Name");
        this.description = ensureNotBlank(description, "Description");
        this.hersteller = ensureNotBlank(hersteller, "Hersteller");
        this.inStock = ensureThatNumberIsHigherThanX(0, inStock, "In-Stock");
        this.preis = ensureThatNumberIsHigherThanX(0.0, preis, "Preis");
        this.kategorie = ensureNotNull(kategorie, "Kategorie");
        this.bildUrl = ensureNotBlank(bildUrl, "BildUrl");

        initFieldNames();
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public static ArtikelBuilder builder() {
        return new ArtikelBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = ensureNotBlank(name, "Name");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = ensureNotBlank(description, "Description");
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = ensureNotBlank(hersteller, "Hersteller");
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = ensureThatNumberIsHigherThanX(0, inStock, "In-Stock");
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = ensureThatNumberIsHigherThanX(0.0, preis, "Preis");
    }

    public String getBildUrl() {
        return bildUrl;
    }

    public void setBildUrl(String bildUrl) {
        this.bildUrl = ensureNotBlank(bildUrl, "BildUrl");
    }

    @Override
    public int compareTo(Artikel o) {
        return this.getName().compareTo(o.getName());
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
        if (Artikel.fieldNames == null) {
            fieldNames = new HashMap<>();
            fieldNames.put("id", "A_ID");
            fieldNames.put("version", "A_Version");
            fieldNames.put("name", "A_Name");
            fieldNames.put("description", "A_Description");
            fieldNames.put("hersteller", "A_Hersteller");
            fieldNames.put("instock", "A_InStock");
            fieldNames.put("preis", "A_Preis");
            fieldNames.put("kategorie", "A_Kategorie");
            fieldNames.put("bildUrl", "A_BildUrl");
        }


    }

    public static String getColumnName(String key) {
        return fieldNames.get(key);
    }

    /******************************/
    /********** builder **********/
    /****************************/

    public static class ArtikelBuilder {


        public String name;
        public String description;
        public String hersteller;
        public Integer inStock;
        public double preis;
        public Kategorie kategorie;
        public String bildUrl;


        public ArtikelBuilder() {

        }

        public ArtikelBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ArtikelBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ArtikelBuilder withHersteller(String hersteller) {
            this.hersteller = hersteller;
            return this;
        }

        public ArtikelBuilder withInStock(Integer inStock) {
            this.inStock = inStock;
            return this;
        }

        public ArtikelBuilder withPreis(double preis) {
            this.preis = preis;
            return this;
        }

        public ArtikelBuilder withKategorie(Kategorie kategorie) {
            this.kategorie = kategorie;
            return this;
        }

        public ArtikelBuilder withBildUrl(String bildUrl) {
            this.bildUrl = bildUrl;
            return this;
        }

        public Artikel build() {
            return new Artikel(name, description, hersteller, inStock, preis, kategorie, bildUrl);
        }

    }

}