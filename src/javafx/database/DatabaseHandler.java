package javafx.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author marin
 */
public class DatabaseHandler {
    
    private static DatabaseHandler handler;
    
    private static String dbURL = "jdbc:derby:database;create=true";
    private final String dbDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static Connection conn = null;
    private static Statement stmt = null;
    
    public DatabaseHandler() {
        createConnection();
        createDefaultTable();
    }
    
    /**
     * Creating database connection
     */
    private void createConnection() {
        
        try {
            Class.forName(dbDriver).newInstance();
            conn = DriverManager.getConnection(dbURL);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
    }
    
    /**
     * Creating default table in database.
     */
    private void createDefaultTable() {
        
        final String TABLE_NAME = "PERSON";
        
        try {
            
            stmt = conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + " firstName varchar(30),\n"
                        + " lastName varchar(30),\n"
                        + " email varchar(40) primary key"
                        + ")");
            }
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + " ---> Database setup");
        }
                
    }
    
    /**
     * Executing query
     * @param query
     * @return ResultSet of executed query
     */
    public ResultSet execQuery(String query) {
        
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        
        return result;
    }
    
    /**
     * Executing action on database
     * @param qu Query action for executing
     * @return Boolean if action was successfully executed
     */
    public boolean execAction(String qu) {
        
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            
            return false;
        }
        
    }
    
}
