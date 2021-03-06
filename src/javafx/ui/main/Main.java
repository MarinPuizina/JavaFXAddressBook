package javafx.ui.main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.database.DatabaseHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author marin
 */
public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Instanciating database connection
        DatabaseHandler.getInstance();
        
    }
    
    public static void main(String[] args) {
        launch(args);        
    }

}
