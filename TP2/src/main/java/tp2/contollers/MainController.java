package tp2.contollers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tp2.Utils;

public class MainController extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private ConnectionDialog connectionDialog;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = FXMLLoader.load(this.getClass().getResource("/main.fxml"));
		Utils.loadFonts();
		connectionDialog = new ConnectionDialog();
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("TP2 - Live Chat");
		primaryStage.show();
		connectionDialog.showAndWait();
		
	}
	
	@FXML
	private void initialize() {
	
	}
}
