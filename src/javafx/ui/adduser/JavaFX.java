package javafx.ui.adduser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author marin
 */
public class JavaFX extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        // Naming the Application "window"
        primaryStage.setTitle("Address Book");
        Parent root = FXMLLoader.load(getClass().getResource("AddUser.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
