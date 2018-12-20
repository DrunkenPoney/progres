package tp3.controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tp3.model.Transmission;

public class MainController extends Application {
	
	public PasswordField passField;
	public TextField     userField;
	public AnchorPane    loginPane, reservPanel, clientPanel, roomPanel;
	@FXML
	private ListView reservList;
	
	public static void main(String[] args) throws Exception {
		Transmission.bob();
		launch(args);
	}
	
	@FXML
	public void login(ActionEvent event) {
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/app.fxml"));
		
		stage.setScene(new Scene(root));
		stage.setOnCloseRequest(e -> stage.close());
		stage.show();
	}
	
	@FXML
	private void add(ActionEvent event) {
		Button btn = (Button) event.getSource();
		switch (Component.valueOf((String) btn.getUserData())) {
			case RESERVATION:
				break;
			case ROOM:
				break;
			case CLIENT:
				break;
			default:
		}
	}
	
	@FXML
	private void edit(ActionEvent event) {
		Button btn = (Button) event.getSource();
		
	}
	
	@FXML
	private void remove(ActionEvent event) {
		Button btn = (Button) event.getSource();
		
	}
	
	@FXML
	private void hidePanel(MouseEvent event) {
		System.out.println(event.isConsumed());
		AnchorPane pane;
		if (event.getSource() instanceof AnchorPane)
			pane = (AnchorPane) event.getSource();
		else pane = (AnchorPane) ((Parent) event.getSource()).getUserData();
		pane.toBack();
	}
	
	@FXML
	private void consume(MouseEvent event) {
		event.consume();
	}
	
	public enum Action {ADD, EDIT, DELETE}
	
	public enum Component {
		RESERVATION, ROOM, CLIENT
	}
}
