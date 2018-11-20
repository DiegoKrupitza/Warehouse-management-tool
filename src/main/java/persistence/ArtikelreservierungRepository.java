package persistence;


import domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtikelreservierungRepository extends AbstractJdbcRepository<Artikelreservierung, Long> implements JdbcDateTimeConversionSupport {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());


    @Override
    protected int insert(Connection con, Artikelreservierung entity) throws PersistenceException {

        // Check if the entity is new
        if (!entity.isNew()) {
            throw new IllegalStateException("Reservierung is already inserted. Update in case of edit"); // is already in database
        }

        // info from entity //

        Integer version = 0;
        Artikel artikel = entity.getArtikel();
        Long artikel_ID = artikel.getId();
        User user = entity.getUser();
        Long user_ID = user.getId();
        String standort = entity.getStandort();
        String abholstatus = entity.getAbholstatus().name();
        Timestamp reservierungsDatum = toDatabaseValue(entity.getReservierungsDatum());


        Timestamp abholdatum = null;

        if (entity.getAbholdatum() != null) {
            abholdatum = toDatabaseValue(entity.getAbholdatum());
        }


        String sql = String.format("INSERT INTO %s" +
                        "(%s,%s,%s,%s,%s,%s,%s)" +
                        "VALUES" +
                        "(?,?,?,?,?,?,?)",
                getTableName(),
                Artikelreservierung.getColumnName("version"),
                Artikelreservierung.getColumnName("user"),
                Artikelreservierung.getColumnName("artikel"),
                Artikelreservierung.getColumnName("standort"),
                Artikelreservierung.getColumnName("abholstatus"),
                Artikelreservierung.getColumnName("resevierungddatum"),
                Artikelreservierung.getColumnName("abholdatum"));

        try {
            PreparedStatement statementInsert = statementManager("Insert_ArtikelReservierung", con, sql);

            statementInsert.setInt(1, version);
            statementInsert.setLong(2, artikel_ID);
            statementInsert.setLong(3, user_ID);
            statementInsert.setString(4, standort);
            statementInsert.setString(5, abholstatus);
            statementInsert.setTimestamp(6, reservierungsDatum);
            statementInsert.setTimestamp(7, abholdatum);

            return statementInsert.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Optional<Artikelreservierung> insertArtikelReservation(Connection con, Artikelreservierung entity) throws Exception {

        return (insert(con, entity) == 0) ? Optional.empty() : findWithMoreFields(con, entity);

        /*if (insert(con, entity) == 0) {
            return findWithMoreFields(con, entity);
        } else {
            return Optional.empty();
        }*/
    }

    @Override
    protected int update(Connection con, Artikelreservierung entity) throws PersistenceException, SQLException {
        if (entity.isNew()) {
            throw new IllegalStateException("Reservierung not existing, insert a Reservation before!");
        }

        // Info from Artikel
        Long id = entity.getId();
        Integer version = entity.getVersion();
        Artikel artikel = entity.getArtikel();
        Long artikel_ID = artikel.getId();
        User user = entity.getUser();
        Long user_ID = user.getId();
        String standort = entity.getStandort();
        String abholstatus = entity.getAbholstatus().name();
        Timestamp reservierungsDatum = toDatabaseValue(entity.getReservierungsDatum());
        Timestamp abholdatum = toDatabaseValue(entity.getAbholdatum());

        Integer newVersion = version++;

        try {
            String updateStatement = String.format("UPDATE %s SET %s = ?,%s = ?,%s = ?,%s = ?,%s = ?,%s = ?, %s = ?" +
                            " WHERE %s = ? AND %s = ?",
                    getTableName(),
                    Artikel.getColumnName("version"),
                    Artikel.getColumnName("user"),
                    Artikel.getColumnName("artikel"),
                    Artikel.getColumnName("standort"),
                    Artikel.getColumnName("abholstatus"),
                    Artikel.getColumnName("resevierungddatum"),
                    Artikel.getColumnName("abholdatum"),
                    getPrimaryKeyColumnName(),
                    Artikel.getColumnName("version"));


            PreparedStatement updatePreparedStmt = statementManager("Update_ArtikelReservierung", con, updateStatement);

            updatePreparedStmt.setInt(1, newVersion.intValue());
            updatePreparedStmt.setLong(2, user_ID);
            updatePreparedStmt.setLong(3, artikel_ID);
            updatePreparedStmt.setString(4, standort);
            updatePreparedStmt.setString(5, abholstatus);
            updatePreparedStmt.setTimestamp(6, reservierungsDatum);
            updatePreparedStmt.setTimestamp(7, abholdatum);
            updatePreparedStmt.setInt(8, version);


            return updatePreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Optional<Artikelreservierung> updateResevierung(Connection con, Artikelreservierung entity) throws Exception {
        update(con, entity);
        return findById(con, entity.getId());
    }

    @Override
    protected String getTableName() {
        return "R_Reservierungen";
    }

    @Override
    protected String getPrimaryKeyColumnName() {
        return "R_ID";
    }

    @Override
    public Optional<Artikelreservierung> findById(Connection con, Long id) throws PersistenceException {
        return (id != null) ? Optional.of(findByX(con, getPrimaryKeyColumnName(), Optional.of(id)).get(0)) : Optional.empty();
    }

    @Override
    public List<Artikelreservierung> findAll(Connection con) throws PersistenceException {
        try {
            String sql = String.format("SELECT * FROM %s", getTableName());
            PreparedStatement statement = statementManager("FindAll_Artikelreservierungen", con, sql);

            return getArtikelreservierungListFromResultSet(con, statement.executeQuery());

        } catch (Exception e) {
            PersistenceException.forException(e);
        }
        return null;
    }


    private Optional<Artikelreservierung> findWithMoreFields(Connection con, Artikelreservierung a) throws Exception {

        String sql = String.format("SELECT * FROM %s WHERE %s = ? and %s = ? and %s = ? and %s = ?",
                getTableName(),
                Artikelreservierung.getColumnName("artikel"),
                Artikelreservierung.getColumnName("standort"),
                Artikelreservierung.getColumnName("resevierungddatum"),
                Artikelreservierung.getColumnName("user"));
        PreparedStatement stmt = statementManager("findWithMoreFields_ArtikelResevierung", con, sql);

        stmt.setInt(1, a.getArtikel().getId().intValue());
        stmt.setString(2, a.getStandort());
        stmt.setTimestamp(3, toDatabaseValue(a.getReservierungsDatum()));
        stmt.setInt(4, a.getUser().getId().intValue());

        return Optional.of(getArtikelreservierungListFromResultSet(con,stmt.executeQuery()).get(0));
    }

    /**
     * @param con
     * @param fieldName
     * @param searchValue
     * @return
     * @throws PersistenceException
     */
    private List<Artikelreservierung> findByX(Connection con, String fieldName, Optional searchValue) throws PersistenceException {

        try {
            String sql = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), fieldName);
            LOGGER.info(sql);
            PreparedStatement stmt = statementManager("FindBy" + fieldName + "_ArtikelResevierung", con, sql);

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

            return getArtikelreservierungListFromResultSet(con,stmt.executeQuery());
        } catch (SQLException e) {
            throw PersistenceException.forSqlException(e);
        } catch (Exception e) {

        }

        return null;
    }




    public Artikelreservierung getArtikelreservierungFromResultSet(Connection con, ResultSet resultSet) {
        Artikelreservierung artikelreservierung = null;

        try {
            Long id = resultSet.getLong(Artikelreservierung.getColumnName("id"));
            Integer version = resultSet.getInt(Artikelreservierung.getColumnName("version"));
            Long userid = resultSet.getLong(Artikelreservierung.getColumnName("user"));
            Long artikelid = resultSet.getLong(Artikelreservierung.getColumnName("artikel"));
            String standort = resultSet.getString(Artikelreservierung.getColumnName("standort"));
            Abholstatus abholstatus = Abholstatus.valueOf(resultSet.getString(Artikelreservierung.getColumnName("abholstatus")));
            LocalDateTime reservierungddatum = fromDatabaseValue(resultSet.getTimestamp(Artikelreservierung.getColumnName("resevierungddatum")));
            Timestamp abholdatum_nullAble = (resultSet.getTimestamp(Artikelreservierung.getColumnName("abholdatum")));
            LocalDateTime abholdatum = null;
            if (abholdatum_nullAble != null) {
                abholdatum = fromDatabaseValue(abholdatum_nullAble);
            }

            Optional<Artikel> a = new ArtikelRepository().findById(con, artikelid);
            Optional<User> u = new UserRepository().findById(con, userid);

            artikelreservierung = Artikelreservierung.builder().withArtikel(a.get()).withUser(u.get()).withStandort(standort).withAbholstatus(abholstatus)
                    .withReservierungsDatum(reservierungddatum).withAbholdatum(abholdatum).build();
            artikelreservierung.setId(id);
            artikelreservierung.setVersion(version);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return artikelreservierung;
    }

    public List<Artikelreservierung> getArtikelreservierungListFromResultSet(Connection con, ResultSet set) throws Exception {
        ArrayList<Artikelreservierung> returnlist = new ArrayList<>();
        while (set.next()) {
            returnlist.add(getArtikelreservierungFromResultSet(con, set));
        }
        return returnlist;
    }


    public List<Artikelreservierung> findByUserID(Connection con, long uId) throws PersistenceException {
        try {
            String sql = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(),Artikelreservierung.getColumnName("user"));
            PreparedStatement statement = statementManager("FindByUserID_Artikelreservierungen", con, sql);
statement.setLong(1,uId);


            return getArtikelreservierungListFromResultSet(con, statement.executeQuery());
        } catch (Exception e) {
            PersistenceException.forException(e);
        }
        return null;
    }

}

