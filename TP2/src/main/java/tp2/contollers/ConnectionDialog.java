package tp2.contollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import tp2.model.utils.Utils;
import tp2.view.ByteTextField;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

import static javafx.beans.binding.Bindings.and;
import static javafx.beans.binding.Bindings.not;

public class ConnectionDialog extends Dialog {
	
	@FXML
	private ByteTextField ipByte1, ipByte2, ipByte3, ipByte4;
	
	@FXML
	private TextField port;
	
	
	public ConnectionDialog() throws IOException, URISyntaxException {
		super();
		StackPane pane = new StackPane();
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(pane);
		loader.load(getClass().getResourceAsStream("/connexion.fxml"));
		
		setTitle("Connexion");
		getDialogPane().getStylesheets().add(getClass().getResource("/css/connexion.css").toExternalForm());
		getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
		getDialogPane().setContent(pane);
		Utils.loadFonts();
		
		setOnCloseRequest(e -> close());
	}
	
	
	private InetSocketAddress getAddress() {
		// TODO
		return null;
	}
	
	
	@FXML
	@SuppressWarnings("unchecked")
	private void initialize() {
		System.out.println("\033[96mINIT!!!\033[39m");
		
		port.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("^\\d{0,5}$") || (newValue.length() > 0 && Integer.parseInt(newValue) > 65535))
				port.setText(oldValue);
		});
		
		getDialogPane().lookupButton(ButtonType.APPLY)
				.disableProperty()
				.bind(not(and(and(ipByte1.getValidProperty(), and(ipByte2.getValidProperty(), and(ipByte3.getValidProperty(), ipByte4.getValidProperty()))), port.textProperty().isNotEmpty())));
		
		setResultConverter(btn -> btn == ButtonType.APPLY ? getAddress() : null);
	}
}
