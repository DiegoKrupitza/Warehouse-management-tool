package persistence;


import domain.Artikel;
import domain.Kategorie;
import domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Generated;
import java.sql.*;
import java.util.*;

public class ArtikelRepository extends AbstractJdbcRepository<Artikel, Long> implements JdbcDateTimeConversionSupport {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Override
    protected int insert(Connection con, Artikel entity) throws PersistenceException {

        // Check if the entity is new
        if (!entity.isNew()) {
            throw new IllegalStateException("User is already inserted. Update in case of edit"); // is already in database
        }

        // Info from Artikel //

        Integer version = 0;
        String name = entity.getName();
        String description = entity.getDescription();
        String hersteller = entity.getHersteller();
        Integer inStock = entity.getInStock();
        Double preis = entity.getPreis();
        String kategorie = entity.getKategorie().name();
        String bildUrl = entity.getBildUrl();


        String sqlQuery = String.format("INSERT INTO %s " +
                        "(%s,%s,%s,%s,%s,%s,%s, %s) " +
                        "VALUES " +
                        "(?,?,?,?,?,?,?,?)",
                getTableName(),
                Artikel.getColumnName("version"),
                Artikel.getColumnName("name"),
                Artikel.getColumnName("description"),
                Artikel.getColumnName("hersteller"),
                Artikel.getColumnName("instock"),
                Artikel.getColumnName("preis"),
                Artikel.getColumnName("kategorie"),
                Artikel.getColumnName("bildUrl"));

        LOGGER.info(sqlQuery);


        try {
            PreparedStatement stmt = statementManager("Insert_Artikel", con, sqlQuery);


            stmt.setInt(1, version.intValue());
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, hersteller);
            stmt.setInt(5, inStock.intValue());
            stmt.setDouble(6, preis.doubleValue());
            stmt.setString(7, kategorie);
            stmt.setString(8, bildUrl);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw PersistenceException.forSqlException(e);
        }


    }

    /**
     * @param con
     * @param artikel
     * @return
     * @throws Exception
     */
    public Artikel insertNewArtikel(Connection con, Artikel artikel) throws Exception {
        return (insert(con, artikel) != 0) ? findByName(con, artikel.getName()) : null;
    }

    /**
     * @param con
     * @param artikelName
     * @return
     * @throws PersistenceException
     * @throws SQLException
     */
    protected int countForName(Connection con, String artikelName) throws PersistenceException, SQLException {

        String sqlQuery = String.format("SELECT Count(*) as Anz from %s WHERE %s = ?", getTableName(), Artikel.getColumnName("name"));
        PreparedStatement stmt = statementManager("CountForName_Artikel", con, sqlQuery);

        stmt.setString(1, artikelName);

        ResultSet res = stmt.executeQuery();

        while (res.next()) {
            return res.getInt("Anz");
        }

        return -1;
    }

    public Artikel updateArtikel(Connection connection, Artikel artikel) throws PersistenceException, SQLException, Exception {
        update(connection, artikel);
        return (findByName(connection, artikel.getName()));
    }

    @Override
    protected int update(Connection con, Artikel entity) throws PersistenceException {
        if (entity.isNew()) {
            throw new IllegalStateException("Artikel is not existing, insert a Artikel before!");
        }

        // Info from Artikel
        Long id = entity.getId();
        String name = entity.getName();
        Integer version = entity.getVersion() + 1;
        String description = entity.getDescription();
        String hersteller = entity.getHersteller();
        Integer inStock = entity.getInStock();
        Double preis = entity.getPreis();
        String kategorie = entity.getKategorie().name();
        String bildUrl = entity.getBildUrl();

        try {
            String updateStatement = String.format("UPDATE %s SET %s = ?,%s = ?,%s = ?,%s = ?,%s = ?,%s = ?, %s = ?,%s = ?" +
                            " WHERE %s = ? AND %s = ?",
                    getTableName(),
                    Artikel.getColumnName("name"), Artikel.getColumnName("version"),
                    Artikel.getColumnName("description"), Artikel.getColumnName("hersteller"),
                    Artikel.getColumnName("instock"), Artikel.getColumnName("preis"),
                    Artikel.getColumnName("kategorie"), Artikel.getColumnName("bildUrl"),
                    getPrimaryKeyColumnName(), Artikel.getColumnName("version"));
            PreparedStatement updatePreparedStmt = statementManager("Update_Artikel", con, updateStatement);

            updatePreparedStmt.setString(1, name);
            updatePreparedStmt.setInt(2, version.intValue());
            updatePreparedStmt.setString(3, description);
            updatePreparedStmt.setString(4, hersteller);
            updatePreparedStmt.setInt(5, inStock);
            updatePreparedStmt.setDouble(6, preis);
            updatePreparedStmt.setString(7, kategorie);
            updatePreparedStmt.setString(8, bildUrl);
            updatePreparedStmt.setLong(9, id);
            updatePreparedStmt.setInt(10, version - 1);

            return updatePreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * @param con
     * @param entity
     * @return
     * @throws Exception
     */
    protected Artikel updateUser(Connection con, Artikel entity) throws Exception {
        return (update(con, entity) != 0) ? findByName(con, entity.getName()) : null;
    }

    @Override
    protected String getTableName() {
        return "A_Artikel";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "A_ID";
    }

    @Override
    public Optional<Artikel> findById(Connection con, Long id) throws PersistenceException {
        return (id != null) ? Optional.of(findByX(con, getPrimaryKeyColumnName(), Optional.of(id)).get(0)) : Optional.empty();
    }

    /**
     * @param con
     * @param fieldName
     * @param searchValue
     * @return
     * @throws PersistenceException
     * @throws SQLException
     */
    private List<Artikel> likeByX(Connection con, String fieldName, String searchValue) throws PersistenceException, SQLException {

        String sql = String.format("SELECT * FROM %s WHERE %s like ?", getTableName(), fieldName);
        LOGGER.info(sql);
        PreparedStatement stmt = statementManager("LikeBy" + fieldName + "_Artikel", con, sql);

        stmt.setString(1, searchValue);

        LOGGER.info(stmt.toString());


        try {
            return getArtikelListFromResultSet(stmt.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    public List<Artikel> likeByName(Connection con, String searchValue) throws SQLException, PersistenceException {
        return likeByX(con, Artikel.getColumnName("name"), searchValue);
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    public List<Artikel> likeByDescription(Connection con, String searchValue) throws SQLException, PersistenceException {
        return likeByX(con, Artikel.getColumnName("description"), searchValue);
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    public List<Artikel> likeByHersteller(Connection con, String searchValue) throws SQLException, PersistenceException {
        return likeByX(con, Artikel.getColumnName("hersteller"), searchValue);
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    public List<Artikel> likeByKategorie(Connection con, String searchValue) throws SQLException, PersistenceException {
        return likeByX(con, Artikel.getColumnName("kategorie"), searchValue);
    }

    /********************/
    /**** name like ****/
    /******************/

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> nameStartsWith(Connection con, String searchValue) throws Exception {
        return likeByName(con, startsWithHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> nameContains(Connection con, String searchValue) throws Exception {
        return likeByName(con, containsHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> nameEndsWith(Connection con, String searchValue) throws Exception {
        return likeByName(con, endsWithHandler(searchValue));
    }

    /***************************/
    /**** Description like ****/
    /*************************/

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> descriptionStartsWith(Connection con, String searchValue) throws Exception {
        return likeByDescription(con, startsWithHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> descriptionContains(Connection con, String searchValue) throws Exception {
        return likeByDescription(con, containsHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> descriptionEndsWith(Connection con, String searchValue) throws Exception {
        return likeByDescription(con, endsWithHandler(searchValue));
    }

    /**************************/
    /**** hersteller like ****/
    /************************/


    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> herstellerStartsWith(Connection con, String searchValue) throws Exception {
        return likeByHersteller(con, startsWithHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> herstellerContains(Connection con, String searchValue) throws Exception {
        return likeByHersteller(con, containsHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> herstellerEndsWith(Connection con, String searchValue) throws Exception {
        return likeByHersteller(con, endsWithHandler(searchValue));
    }

    /*************************/
    /**** kategorie like ****/
    /***********************/


    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> kategorieStartsWith(Connection con, String searchValue) throws Exception {
        return likeByKategorie(con, startsWithHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> kategorieContains(Connection con, String searchValue) throws Exception {
        return likeByKategorie(con, containsHandler(searchValue));
    }

    /**
     * @param con
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Artikel> kategorieEndsWith(Connection con, String searchValue) throws Exception {
        return likeByKategorie(con, endsWithHandler(searchValue));
    }

    /**
     * @param con
     * @param fieldName
     * @param searchValue
     * @return
     * @throws PersistenceException
     */
    private List<Artikel> findByX(Connection con, String fieldName, Optional searchValue) throws PersistenceException {

        try {
            String sql = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), fieldName);
            LOGGER.info(sql);
            PreparedStatement stmt = statementManager("FindBy" + fieldName + "_Artikel", con, sql);

            if (searchValue.get() instanceof Integer) {
                stmt.setInt(1, ((Integer) searchValue.get()).intValue());
            } else if (searchValue.get() instanceof String) {
                stmt.setString(1, searchValue.get().toString());
            } else if (searchValue.get() instanceof Long) {
                stmt.setLong(1, ((Long) searchValue.get()).longValue());
            } else if (searchValue.get() instanceof Kategorie) {
                stmt.setString(1, ((Kategorie) searchValue.get()).name());
            }

            LOGGER.info(stmt.toString());

            return getArtikelListFromResultSet(stmt.executeQuery());
        } catch (SQLException e) {
            throw PersistenceException.forSqlException(e);
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * @param con
     * @param name
     * @return a spezifik artikel object with a spezific name
     * @throws Exception
     */
    protected Artikel findByName(Connection con, String name) throws Exception {
        return (name != null) ? findByX(con, Artikel.getColumnName("name"), Optional.of(name)).get(0) : null;
    }

    /**
     * @param con
     * @param kategorie
     * @return a list of Artikels with the Kategorie x
     * @throws Exception
     */
    public List<Artikel> findByKategorie(Connection con, Kategorie kategorie) throws Exception {
        return (kategorie != null) ? findByX(con, Artikel.getColumnName("kategorie"), Optional.of(kategorie)) : null;
    }

    public List<Artikel> findByDescription(Connection con, String description) throws Exception {
        return (description != null) ? findByX(con, Artikel.getColumnName("description"), Optional.of(description)) : null;
    }

    @Override
    public List<Artikel> findAll(Connection con) throws PersistenceException {
        try {
            String sql = String.format("SELECT * FROM %s", getTableName());
            PreparedStatement stmt = statementManager("FindAll_Artikel", con, sql);

            return getArtikelListFromResultSet(stmt.executeQuery());
        } catch (Exception e) {
            PersistenceException.forException(e);
        }
        return null;
    }

    /**
     * @param res
     * @return Artikel object from Resultset
     */
    public Artikel getArtikelFromResultSet(ResultSet res) {
        Artikel a = null;
        try {

            Long id = res.getLong(Artikel.getColumnName("id"));
            Integer version = res.getInt(Artikel.getColumnName("version"));

            String name = res.getString(Artikel.getColumnName("name"));
            String description = res.getString(Artikel.getColumnName("description"));
            String hersteller = res.getString(Artikel.getColumnName("hersteller"));
            Integer inStock = res.getInt(Artikel.getColumnName("instock"));
            Double preis = res.getDouble(Artikel.getColumnName("preis"));
            Kategorie kategorie = Kategorie.valueOf(res.getString(Artikel.getColumnName("kategorie")));
            String bildUrl = res.getString(Artikel.getColumnName("bildUrl"));

            a = Artikel.builder().withName(name).withDescription(description).withHersteller(hersteller).withInStock(inStock).withPreis(preis).withKategorie(kategorie).withBildUrl(bildUrl).build();
            a.setId(id);
            a.setVersion(version);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return a;
    }

    /**
     * @param set
     * @return
     * @throws Exception
     */
    public List<Artikel> getArtikelListFromResultSet(ResultSet set) throws Exception {


        ArrayList<Artikel> returnList = new ArrayList<>();
        while (set.next()) {
            returnList.add(getArtikelFromResultSet(set));
        }
        return returnList;

    }

}
