package tp1.partie3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader   loader     = new FXMLLoader();
        Parent       root       = loader.load(getClass().getResourceAsStream("/Partie3.fxml"));
        ControllerP3 controller = loader.getController();
        primaryStage.setOnCloseRequest(controller::exit);
        primaryStage.setTitle("TP01 - Partie 3");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
