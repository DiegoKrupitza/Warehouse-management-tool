package persistence;


import java.sql.PreparedStatement;
import java.util.HashMap;

/**
 * StatementTyp which type of Statement zb CountForName
 * Table which Table is affected zb U_User
 */
public class Statements {

    static HashMap<String, PreparedStatement> stmtHasMap = new HashMap<>();

    public static PreparedStatement getStmt(String key) {
        return stmtHasMap.get(key);
    }

    public static boolean hasStmt(String key) {
        return stmtHasMap.containsKey(key);
    }

    public static void storeStmt(String key,PreparedStatement val) {
        if(hasStmt(key)) {
            throw new IllegalArgumentException("This Key already exist! Use replace method");
        }else {
            stmtHasMap.put(key,val);
        }
    }

    public static void replace(String key,PreparedStatement val) {
        if(!hasStmt(key)) {
            throw new IllegalArgumentException("This Key doesn't exist! Use storeStmt method");
        }else {
            stmtHasMap.replace(key,val);
        }
    }

    public static HashMap<String, PreparedStatement> getStmtHashMap() {
        return stmtHasMap;
    }

    private static void setStmtHasMap(HashMap<String, PreparedStatement> stmtHasMap) {
        Statements.stmtHasMap = stmtHasMap;
    }
}
