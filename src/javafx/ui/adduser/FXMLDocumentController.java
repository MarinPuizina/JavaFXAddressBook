package javafx.ui.adduser;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author marin
 */
public class FXMLDocumentController implements Initializable {
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setting up database connection and creating table
        databaseHandler = new DatabaseHandler();
    }    

    @FXML
    private void addUser(ActionEvent event) {
    }

    @FXML
    private void cancel(ActionEvent event) {
    }
    
}
