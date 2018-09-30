package tp2.contollers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tp2.model.db.collections.Accessors;
import tp2.model.db.collections.LocalClientCollection;
import tp2.model.db.documents.ClientModel;
import tp2.model.io.Receiver;
import tp2.model.utils.Utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainController extends Application {
	
	public static void main(String[] args) throws Exception {
		//		launch(args);
		Receiver receiver = new Receiver();
		Accessors.initLocalClient("Bob", receiver.getChannel());
		
		receiver.addHandler((message, client, attachment) -> System.out.println(
				String.format("%s (%s) sent: %s", attachment.getName(), client.toString(), message)));
		
		List<ClientModel> clients = Accessors.getClientCollection().getOnlineClients();
		boolean modelRemoved = clients.removeIf(model -> model
				.equals(((LocalClientCollection) Accessors.getClientCollection()).getLocalClient()));
		
		System.out.println(modelRemoved ? "A model has been removed from the list" : "No model removed");
		System.out.println("Online clients:\n\t> " + clients.stream()
				.map(client -> String.format("%s (%s)", client.getName(), client.getAddress())).collect(
						Collectors.joining("\n\t> ")));
		
//		Sender sender = new Sender(((LocalClientCollection)
//				CollectionAccessors.getClientCollection()).getLocalClient());
//		sender.send("Hey! Do you copy?", ((ClientModel) clients.toArray()[0]).getSocket());
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
