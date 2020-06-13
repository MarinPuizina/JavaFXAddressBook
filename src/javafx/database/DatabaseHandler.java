package javafx.database;

import javafx.xml.XMLHandler;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.ui.listuser.ListUserController.User;
import javax.swing.JOptionPane;

/**
 *
 * @author marin
 */
public class DatabaseHandler {

    private static DatabaseHandler databaseHandler;

    private static String dbURL = "jdbc:derby:database;create=true";
    private final String dbDriver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static Connection conn = null;
    private static Statement stmt = null;
    private final String TABLE_NAME = "PERSON";

    private DatabaseHandler() {
        createConnection();
        createDefaultTable();
    }

    /**
     * Static factory method
     *
     * @return Instance of database handler
     */
    public static DatabaseHandler getInstance() {

        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler();
        }

        return databaseHandler;
    }

    /**
     * Creating database connection
     */
    private void createConnection() {

        try {
            Class.forName(dbDriver).newInstance();
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    /**
     * Creating default table in database.
     */
    private void createDefaultTable() {

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
     *
     * @param query
     * @return ResultSet of executed query
     */
    public ResultSet execQuery(String query) {

        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }

        return result;
    }

    /**
     *
     * @param userFirstName
     * @param userLastName
     * @param userEmail
     * @return
     */
    public boolean execAction(String userFirstName, String userLastName, String userEmail) {

        String qu = "INSERT INTO PERSON VALUES ("
                + "'" + userFirstName + "',"
                + "'" + userLastName + "',"
                + "'" + userEmail + "'"
                + ")";

        System.out.println(qu);

        try {

            stmt = conn.createStatement();
            stmt.execute(qu);

            return true;
        } catch (SQLException ex) {
            System.out.println("DB Error code --> " + ex.getErrorCode());
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());

            System.out.printf("Added the person %s %s %s into the Persons.xml", userFirstName, userLastName, userEmail);
            System.out.println();
            new XMLHandler().addElement(userFirstName, userLastName, userEmail);

            return false;
        }

    }

    /**
     * Delete User from database
     * 
     * @param email
     * @return 
     */
    public boolean deleteUser(String email) {

        String deleteStatement = "DELETE FROM " + TABLE_NAME + " WHERE EMAIL = ?";
        try {

            PreparedStatement preparedStatement = conn.prepareStatement(deleteStatement);
            preparedStatement.setString(1, email);
            int res = preparedStatement.executeUpdate();

            if (res == 1) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Update User in database
     * 
     * @param user
     * @return 
     */
    public boolean updateUser(User user) {

        try {

            String update = "UPDATE " + TABLE_NAME + " SET FIRSTNAME = ?, LASTNAME = ? WHERE EMAIL = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(update);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            int result = preparedStatement.executeUpdate();

            return (result > 0);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Find User in database
     * 
     * @param email
     * @return 
     */
    public boolean findUser(String email) {

        try {

            String findUser = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE EMAIL = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(findUser);
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                final int count = result.getInt(1);
                return count == 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

}
