package tp1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TP1Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        TP1Controller controller = new TP1Controller();
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(1000);
        primaryStage.setOnCloseRequest(controller::exit);
        primaryStage.setTitle("TP01");
        primaryStage.setScene(new Scene(controller));
        primaryStage.show();
    }
}
