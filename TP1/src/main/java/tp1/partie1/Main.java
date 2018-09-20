package tp1.partie1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(System.getProperties().get("javafx.runtime.version"));
        ControllerP1 root = new ControllerP1();
        primaryStage.setOnCloseRequest(root::exit);
        primaryStage.setTitle("TP01 - Partie 1");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
