package tp1.partie2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        ControllerP2 root = new ControllerP2();
        primaryStage.setOnCloseRequest(root::exit);
        primaryStage.setTitle("TP01 - Partie 2");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
