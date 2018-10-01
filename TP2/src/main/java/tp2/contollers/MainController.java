package tp2.contollers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tp2.models.utils.Utils;

public class MainController extends Application {
	
	public static void main(String[] args) {
			launch(args);
	}
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(this.getClass().getResource("/main.fxml"));
		Utils.loadFonts();
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("TP2 - Live Chat");
		primaryStage.setOnCloseRequest(windowEvent -> primaryStage.close());
		primaryStage.show();

//		ConnectionDialog dialog = new ConnectionDialog();
//		dialog.showAndWait();
	}
	
	@FXML
	private void initialize() {
	
	}
}
