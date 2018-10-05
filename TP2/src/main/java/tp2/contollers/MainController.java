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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tp2.models.db.collections.Accessors;
import tp2.models.db.collections.LocalClientCollection;
import tp2.models.db.documents.GroupModel;
import tp2.models.db.internals.exceptions.InvalidAttributeException;
import tp2.models.io.Transmission;
import tp2.models.io.data.EventData;
import tp2.models.io.data.EventData.EventType;
import tp2.models.io.data.FileData;
import tp2.models.io.data.MessageData;
import tp2.models.utils.Constants;
import tp2.models.utils.I18n.Language;
import tp2.models.utils.SGR;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.*;
import static tp2.models.db.collections.Accessors.getClientsCollection;
import static tp2.models.db.collections.Accessors.getLocalClientCollection;
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
	private Label groupLabel, filesLabel;
	@FXML
	private Button btnLanguage, btnSend;
	
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
		setLanguage(System.getProperty("user.language").equalsIgnoreCase("en")
		            ? Language.EN : Language.FR);
		btnSend.disableProperty().bind(leftPanel.selectedGroupProperty().isNull());
		Transmission.getInstance().addDataHandler(data -> Platform.runLater(() -> {
			if (data instanceof MessageData)
				chatMsg((MessageData) data);
			else if (data instanceof FileData) {
				files.getItems().add((FileData) data);
				chatLog(data.getSender().getName(),
				        format(messages().get("event.file.sent"), ((FileData) data).getFileName()));
			} else if (data instanceof EventData)
				chatLog(data.getSender().getName(), ((EventData) data).getEvent() == EventType.GROUP_JOIN
				                                    ? messages().get("event.group.join")
				                                    : messages().get("event.group.left"));
		}));
		leftPanel.selectedGroupProperty().addListener((o, oldValue, newValue) -> {
			assert getLocalClientCollection() != null : "LocalClientCollection hasn't been initialized";
			if (oldValue != null)
				Transmission.getInstance().send(new EventData(getLocalClientCollection().getLocalClient(),
				                                              EventType.GROUP_LEFT, oldValue));
			if (newValue == null) {
				chatLog(messages().get("chat.log.group.joined.null"));
				groupLabel.setText(messages().get("label.group.default"));
			} else {
				Transmission.getInstance().send(new EventData(getLocalClientCollection().getLocalClient(),
				                                              EventType.GROUP_JOIN, newValue));
				chatLog(format(messages().get("chat.log.group.joined"), newValue.getName()));
				groupLabel.setText(newValue.getName());
			}
			getLocalClientCollection().getLocalClient().setGroup(newValue);
			getLocalClientCollection().saveLocalClient();
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
		chatBox.getContextMenu().setPrefWidth(40);
		
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
	private void switchLanguage(ActionEvent event) {
		if (((Button) event.getSource()).getText().equalsIgnoreCase(Language.FR.getLang()))
			setLanguage(Language.FR);
		else setLanguage(Language.EN);
	}
	
	private void setLanguage(Language lang) {
		Locale.setDefault(lang.getLocale());
		btnSend.setText(messages().get("btn.send.text"));
		groupLabel.setText(messages().get("label.group.default"));
		filesLabel.setText(messages().get("label.files"));
		btnLanguage.setText(lang.equals(Language.EN) ? Language.FR.getLang() : Language.EN.getLang());
		leftPanel.applyLanguage(lang);
	}
	
	@FXML
	@SuppressWarnings("unused")
	private void send(ActionEvent event) {
		assert getLocalClientCollection() != null : "LocalClientCollection hasn't been initialized";
		if (isNotBlank(textField.getCharacters())) {
			Transmission.getInstance()
					.send(new MessageData(getLocalClientCollection().getLocalClient(),
					                      textField.getText(),
					                      leftPanel.selectedGroupProperty().get()));
			textField.setText("");
		}
	}
	
	private void chatLog(String name, String action, String... more) {
		Platform.runLater(() -> {
			Label n = new Label(name);
			n.setStyle("-fx-text-fill: aquamarine");
			n.setWrapText(true);
			Label act = new Label(action + String.join("", more));
			act.setStyle("-fx-text-fill: lightgray");
			act.setWrapText(true);
			chatBox.getItems().add(new HBox(n, act));
		});
	}
	
	private void chatLog(String log) {
		Platform.runLater(() -> {
			Label entry = new Label(log);
			entry.setStyle("-fx-text-fill: lightgray");
			entry.setWrapText(true);
			HBox.setHgrow(entry, Priority.SOMETIMES);
			chatBox.getItems().add(new HBox(entry));
		});
	}
	
	private void chatMsg(MessageData msg) {
		Platform.runLater(() -> {
			Label sender = new Label(msg.getSender().getName() + " :");
			sender.setStyle("-fx-font-weight: bold; -fx-text-fill: greenyellow");
			sender.setWrapText(true);
			Label message = new Label(msg.getMessage());
			message.setStyle("-fx-text-fill: cadetblue");
			message.setWrapText(true);
			VBox box = new VBox(sender, message);
			HBox.setHgrow(box, Priority.SOMETIMES);
			chatBox.getItems().add(new HBox(box));
		});
	}
	
	private void promptForName(Stage stage) throws IOException, InvalidAttributeException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText(messages().get("dialog.name.choice.header"));
		dialog.setTitle(messages().get("dialog.name.choice.title"));
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
