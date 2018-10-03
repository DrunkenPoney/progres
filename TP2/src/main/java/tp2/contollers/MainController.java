package tp2.contollers;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tp2.models.db.collections.Accessors;
import tp2.models.db.collections.LocalClientCollection;
import tp2.models.db.documents.GroupModel;
import tp2.models.db.internals.exceptions.InvalidAttributeException;
import tp2.models.io.FileData;
import tp2.models.io.Message;
import tp2.models.io.Transmission;
import tp2.models.utils.Constants;
import tp2.models.utils.SGR;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.*;
import static tp2.models.db.collections.Accessors.getClientsCollection;
import static tp2.models.utils.I18n.messages;

@SuppressWarnings("WeakerAccess")
public class MainController extends Application {
	@FXML
	private ListView<HBox>      chatBox;
	@FXML
	private TextField           textField;
	@FXML
	private LeftPanelController leftPanel;
	@FXML
	private AnchorPane          contentPane;
	@FXML
	private ListView<FileData>  files;
	@FXML
	private Button              btnSend;
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void main(String[] args) {
		Transmission.getInstance();
		// Permet d'initialiser la connexion à la base de données
		getClientsCollection();
		launch(args);
	}
	
	public static void print(String key, Object value) {
		System.out.println(SGR.FG_BRIGHT_GREEN.wrap(key) +
		                   SGR.FG_BRIGHT_YELLOW.wrap(" => ") +
		                   SGR.FG_BRIGHT_CYAN.wrap(value));
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(Constants.RSS_FXML_MAIN.get());
		root.getStylesheets().add(Constants.RSS_CSS_MAIN.getPath());
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle(messages().get("window.title"));
		primaryStage.setOnCloseRequest(windowEvent -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			primaryStage.close();
			System.exit(0);
		});
		primaryStage.show();
		
		promptForName(primaryStage);
	}
	
	@FXML
	private void initialize() {
		btnSend.disableProperty().bind(leftPanel.selectedGroupProperty().isNull());
		Transmission.getInstance().addDataHandler(data -> {
			if (data instanceof Message)
				chatMsg((Message) data);
			else if (data instanceof FileData)
				files.getItems().add((FileData) data);
		});
		leftPanel.selectedGroupProperty().addListener((o, oldValue, newValue) -> {
			if (newValue == null)
				chatLog(messages().get("chat.log.group.joined.null"));
			else chatLog(format(messages().get("chat.log.group.joined"), newValue.getName()));
		});
		
		leftPanel.prefWidthProperty()
		         .addListener((observable, oldValue, newValue) ->
				                      AnchorPane.setLeftAnchor(contentPane, newValue.doubleValue()));
		AnchorPane.setLeftAnchor(contentPane, leftPanel.getPrefWidth());
		
		MenuItem sendFile = new MenuItem(messages().get("chat.context.menu.send.file"));
		sendFile.setOnAction(event -> {
			final GroupModel group = leftPanel.selectedGroupProperty().get();  
			if (group != null) {
				FileChooser chooser = new FileChooser();
				List<File>  files   = chooser.showOpenMultipleDialog(chatBox.getScene().getWindow());
				if (files != null && files.size() > 0) {
					Flowable.fromIterable(files)
					        .parallel()
					        .runOn(Schedulers.io())
					        .map(file -> new FileData(
							        ((LocalClientCollection) getClientsCollection()).getLocalClient(),
							        file.getName(), Files.readAllBytes(file.toPath()),
							        group))
					        .doOnNext(file -> Transmission.getInstance().send(file))
					        .sequential()
					        .subscribe();
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(messages().get("error.send.no.group.header"));
				alert.setHeaderText(messages().get("error.send.no.group.content"));
				alert.showAndWait();
			}
		});
		chatBox.setContextMenu(new ContextMenu(sendFile));
		
		files.setCellFactory(params -> new ListCell<>() {
			@Override
			protected void updateItem(FileData item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getFileName());
					MenuItem saveFile = new MenuItem(messages().get("files.context.menu.save"));
					saveFile.setOnAction(event -> {
						FileChooser chooser = new FileChooser();
						chooser.setInitialFileName(item.getFileName());
						File file = chooser.showSaveDialog(getScene().getWindow());
						if (file != null) {
							try {
								Files.write(file.toPath(), item.getFileContent(),
								            StandardOpenOption.WRITE,
								            StandardOpenOption.CREATE,
								            StandardOpenOption.TRUNCATE_EXISTING);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
					MenuItem remove = new MenuItem(messages().get("files.context.menu.remove"));
					remove.setOnAction(event -> files.getItems().remove(item));
					setContextMenu(new ContextMenu(saveFile, remove));
				}
			}
		});
		
		
	}
	
	@FXML
	@SuppressWarnings("unused")
	private void send(ActionEvent event) {
		Transmission.getInstance().send(new Message(((LocalClientCollection) getClientsCollection()).getLocalClient(),
		                                            textField.getText(),
		                                            leftPanel.selectedGroupProperty().get()));
		textField.setText("");
	}
	
	private void chatLog(String log) {
		Platform.runLater(() -> {
			Text entry = new Text(log);
			entry.setFill(Color.LIGHTGRAY);
			chatBox.getItems().add(new HBox(entry));
		});
	}
	
	private void chatMsg(Message msg) {
		Platform.runLater(() -> {
			Text sender = new Text(msg.getSender().getName());
			sender.setFill(Color.GREENYELLOW);
			Text info = new Text(messages().get("chat.msg.sent"));
			info.setFill(Color.LIGHTGRAY);
			Text message = new Text(msg.getMessage());
			message.setFill(Color.AQUA);
			HBox box = new HBox(sender, info, message);
			box.setSpacing(3);
			chatBox.getItems().add(box);
		});
	}
	
	private void promptForName(Stage stage) throws IOException, InvalidAttributeException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText(messages().get("dialog.name.choice.header"));
		dialog.setTitle(messages().get("dialog.name.choice.title"));
		dialog.getDialogPane().getContent().setStyle("-fx-text-fill: red");
		dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
		      .bind(dialog.getEditor().textProperty().isEmpty());
		dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
			if (length(trim(deleteWhitespace(newValue))) != length(newValue))
				dialog.getEditor().setText(trim(deleteWhitespace(newValue)));
		});
		dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
		dialog.setResultConverter(param -> {
			if (param == ButtonType.CANCEL) {
				stage.close();
				System.exit(0);
			}
			return dialog.getEditor().getText();
		});
		
		boolean available = false;
		String  name      = null;
		while (!available) {
			if (name != null)
				dialog.setContentText(messages().get("error.name.already.taken"));
			name = dialog.showAndWait().orElse(null);
			available = name != null && getClientsCollection().isNameAvailable(name);
		}
		Accessors.initLocalClient(name, (InetSocketAddress) Transmission.getInstance().getLocalAddress());
	}
}
