package tp1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tp1.partie1.ControllerP1;
import tp1.partie2.ControllerP2;
import tp1.partie3.ControllerP3;

import java.io.IOException;
import java.net.InetAddress;

public class TP1Controller extends HBox implements IBaseContoller {
    
    @FXML
    public ControllerP1 partie1;
    
    @FXML
    public ControllerP2 partie2;
    
    @FXML
    public ControllerP3 partie3;
    
    public TP1Controller() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(this);
        loader.setController(this);
        loader.load(getClass().getResourceAsStream("/TP1.fxml"));
        this.getStylesheets().add(getClass().getResource("/styles.css").toString());
    }
    
    @Override
    public void exit(WindowEvent event) {
        partie3.exit(event);
        partie2.exit(event);
        partie1.exit(event);
    }
    
    @FXML
    private void initialize() {
        partie2.getIPAddressList().setCellFactory(p -> new ControllerP2.IPAddressListCell() {
            @Override
            protected void updateItem(InetAddress address, boolean empty) {
                super.updateItem(address, empty);
    
                if (!empty && address != null) {
                    MenuItem scanItem = new MenuItem("Scanner les ports");
                    scanItem.setOnAction(event -> partie3.scanAddress(address));
                    getContextMenu().getItems().add(scanItem);
                }
            }
        });
    }
}
