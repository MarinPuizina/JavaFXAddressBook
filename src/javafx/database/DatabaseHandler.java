package javafx.database;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author marin
 */
public class DatabaseHandler {
    
    private static DatabaseHandler handler;
    
    private static String dbURL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    final String dbDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    
    /**
     * Method for creating database connection
     */
    void createConnection() {
        
        try {
            Class.forName(dbDriver).newInstance();
            conn = DriverManager.getConnection(dbURL);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
    }
    
}
