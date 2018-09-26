package tp2.contollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import tp2.view.ByteTextField;

import java.io.File;
import java.io.IOException;

public class ConnectionDialog extends Dialog {
	
	@FXML
	private ByteTextField ipByte1, ipByte2, ipByte3, ipByte4;
	
	@FXML
	private TextField port;
	
	@FXML
	private Button applyBtn;
	
	
	public ConnectionDialog() throws IOException {
		super();
		DialogPane dialogPane = new DialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("/css/connexion.css").toExternalForm());
		this.setDialogPane(dialogPane);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(dialogPane);
		loader.load(getClass().getResourceAsStream("/connexion.fxml"));
	}
	
	
	@FXML
	private void initialize() {
		setOnCloseRequest(event -> close());
		
		port.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("^\\d{0,5}$") || (newValue.length() > 0 && Integer.parseInt(newValue) > 65535))
				port.setText(oldValue);
		});
		
	}
	
	
}
