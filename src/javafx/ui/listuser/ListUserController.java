package javafx.ui.listuser;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.ui.adduser.AddUserController;
import javafx.ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author marin
 */
public class ListUserController implements Initializable {

    ObservableList<User> list = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initColumns();
        loadData();

    }

    /**
     * Populating cell values with data
     */
    private void initColumns() {

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    /**
     * Loading data to be displayed in List Users window
     */
    private void loadData() {

        list.clear();
        
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

        String qu = "SELECT * FROM PERSON";
        ResultSet resultSet = databaseHandler.execQuery(qu);

        try {

            while (resultSet.next()) {

                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");

                list.add(new User(firstName, lastName, email));

            }

        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);

    }

    /**
     * Deleting user from List User window
     * 
     * @param event 
     */
    @FXML
    private void handleUserDeleteOption(ActionEvent event) {

        User selectedForDeletion = tableView.getSelectionModel().getSelectedItem();

        if (selectedForDeletion == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("User was not selected, please select an user.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Deleting User");
        alert.setContentText("Are you sure you want to delete the user: " + selectedForDeletion.getEmail());
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {

            Boolean result = DatabaseHandler.getInstance().deleteUser(selectedForDeletion.getEmail());

            if (result) {

                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setHeaderText(null);
                cancelAlert.setContentText("User has been deleted");
                cancelAlert.showAndWait();
                
                list.remove(selectedForDeletion);
                

            } else {

                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setHeaderText(null);
                cancelAlert.setContentText("Delete process has failed");
                cancelAlert.showAndWait();
                
            }

        } else {

            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
            cancelAlert.setHeaderText(null);
            cancelAlert.setContentText("Delete process canceled");
            cancelAlert.showAndWait();

        }

    }

    /**
     * Editing user from List User window
     * 
     * @param event 
     */
    @FXML
    private void handleUserEditOption(ActionEvent event) {
        
        User selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        
        if (selectedForEdit == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("User was not selected, please select an user.");
            alert.showAndWait();

            return;
        }
        
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/ui/adduser/AddUser.fxml"));
            Parent parent = loader.load();
            
            AddUserController controller = (AddUserController) loader.getController();
            controller.inflateUI(selectedForEdit);
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit User");
            stage.setScene(new Scene(parent));
            stage.show();
            
            stage.setOnCloseRequest((e) -> {
                handleRefreshOption(new ActionEvent());
            });
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     * Refreshing data on List Users window
     * 
     * @param event 
     */
    @FXML
    private void handleRefreshOption(ActionEvent event) {
        loadData();
    }

    /**
     * User Object
     */
    public static class User {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;

        public User(String firstName, String lastName, String email) {

            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.email = new SimpleStringProperty(email);

        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getLastName() {
            return lastName.get();
        }

        public String getEmail() {
            return email.get();
        }

    }

}
