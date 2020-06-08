package javafx.ui.adduser;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author marin
 */
public class AddUserController implements Initializable {
    
    private Label label;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    
    DatabaseHandler databaseHandler;
    
    @FXML
    private AnchorPane rootPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setting up database connection and creating table
        databaseHandler = DatabaseHandler.getInstance();
        checkData();
    }    

    @FXML
    private void addUser(ActionEvent event) {
        
        String userFirstName = firstName.getText();
        String userLastName = lastName.getText();
        String userEmail = email.getText();
        
        if(userFirstName.isEmpty() || userLastName.isEmpty() || userEmail.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter information in all fields.");
            alert.showAndWait();
            
        } else {
          
            String qu = "INSERT INTO PERSON VALUES ("
                    + "'" + userFirstName + "',"
                    + "'" + userLastName + "',"
                    + "'" + userEmail + "'"
                    + ")";
            
            System.out.println(qu);
            
            if(databaseHandler.execAction(qu)) {
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success");
                alert.showAndWait();
                
            } else {
                
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed");
                alert.showAndWait();
                
            }
            
        }
        
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private void checkData() {
        
        String qu = "SELECT email FROM PERSON";
        ResultSet resultSet = databaseHandler.execQuery(qu);
        
        
        try {
                
            while(resultSet.next()) {
                    String emailCheck = resultSet.getString("email");
                    System.out.println(emailCheck);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
