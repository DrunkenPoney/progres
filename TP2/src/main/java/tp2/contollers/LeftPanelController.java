package tp2.contollers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;
import tp2.models.utils.Constants;

import java.io.IOException;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static tp2.contollers.MainController.print;
import static tp2.models.db.collections.Accessors.*;
import static tp2.models.utils.Constants.LEFT_PANEL_WIDTH;
import static tp2.models.utils.Constants.WIDTH_ANIMATION_DURATION;
import static tp2.models.utils.I18n.messages;

@SuppressWarnings("unused")
public class LeftPanelController extends AnchorPane {
	public static final String PANEL_TOGGLE_BTN_TEXT_OPENED = "\uD83E\uDC47";
	public static final String PANEL_TOGGLE_BTN_TEXT_CLOSED = "\uD83E\uDC45";
	public static final String ADD_GROUP_BTN_TEXT           = "\uD83D\uDFA4";
	
	@FXML
	private ListView<GroupModel> groupList;
	
	@FXML
	private ListView<ClientModel> clientList;
	
	@FXML
	private Button btnAddGroup;
	
	@FXML
	private Button btnTogglePanel;
	
	
	public LeftPanelController() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(this);
		loader.load(Constants.RSS_FXML_LEFT_PANEL.getStream());
	}
	
	@FXML
	private void initialize() {
		groupList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		clientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		groupList.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(GroupModel item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getName());
				}
			}
		});
		
		clientList.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(ClientModel item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(format("%s (%s:%s)", item.getName(), item.getAddress(), item.getPort()));
				}
			}
		});
		
		groupList.getItems().addAll(getGroupsCollection().list());
		
		getGroupsCollection().addListener((oldList, newList) -> Platform.runLater(() -> {
			GroupModel group = groupList.getSelectionModel().getSelectedItem();
			groupList.getItems().retainAll(newList);
			newList.removeAll(groupList.getItems());
			groupList.getItems().addAll(newList);
			groupList.getSelectionModel().select(group);
		}));
		
		getClientsCollection().addListener((oldList, newList) -> Platform.runLater(() -> {
			clientList.getItems().retainAll(newList);
			newList.removeAll(clientList.getItems());
			clientList.getItems().addAll(newList);
		}));
	}
	
	@FXML
	private void togglePanel(ActionEvent event) {
		Button btn = (Button) event.getSource();
		if (btn.getText().equals(PANEL_TOGGLE_BTN_TEXT_CLOSED)) {
			print("Panel action", "OPENING");
			btn.setText(PANEL_TOGGLE_BTN_TEXT_OPENED);
			setWidthAnimated(LEFT_PANEL_WIDTH);
		} else {
			print("Panel action", "CLOSING");
			btn.setText(PANEL_TOGGLE_BTN_TEXT_CLOSED);
			setWidthAnimated(0);
		}
	}
	
	private void setWidthAnimated(double width) {
		new Timeline(new KeyFrame(WIDTH_ANIMATION_DURATION,
		                          new KeyValue(prefWidthProperty(), width))).play();
	}
	
	public ReadOnlyObjectProperty<GroupModel> selectedGroupProperty() {
		return groupList.getSelectionModel().selectedItemProperty();
	}
	
	public ReadOnlyObjectProperty<ClientModel> selectedClientProperty() {
		return clientList.getSelectionModel().selectedItemProperty();
	}
	
	@FXML
	private void createGroup(ActionEvent event) {
		assert getLocalClientCollection() != null : "LocalClientCollection hasn't been initialized";
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(messages().get("dialog.group.creation.title"));
		dialog.setHeaderText(messages().get("dialog.group.creation.header"));
		String groupName = dialog.showAndWait().orElse(null);
		if (isNotBlank(groupName)) {
			GroupModel group = new GroupModel(groupName);
			getGroupsCollection().save(group);
			getLocalClientCollection().getLocalClient().setGroup(group);
			getLocalClientCollection().saveLocalClient();
			groupList.getItems().add(group);
			groupList.getSelectionModel().select(group);
			groupList.refresh();
		}
	}
}
