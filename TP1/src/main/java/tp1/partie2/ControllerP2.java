package tp1.partie2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import tp1.IBaseContoller;
import tp1.models.NetworkScanner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

public class ControllerP2 extends AnchorPane implements IBaseContoller {
    private final NetworkScanner scanner;
    
    @FXML
    protected ListView<InetAddress> ipList;
    
    @FXML
    protected TextField nameField;
    
    public ControllerP2() throws IOException {
        this.scanner = new NetworkScanner();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setRoot(this);
        loader.load(getClass().getResourceAsStream("/Partie2.fxml"));
        this.getStylesheets().add(getClass().getResource("/styles.css").toString());
    }
    
    @FXML
    private void btnFullNameHandler() {
        InetAddress selected = ipList.getSelectionModel().getSelectedItem();
        if (selected != null) { nameField.setText(selected.getCanonicalHostName()); }
    }
    
    @FXML
    private void initialize() throws SocketException {
        ipList.setCellFactory(p -> new IPAddressListCell());
        ipList.setItems(this.scanner.getAddressList());
        scanner.scan();
    }
    
    public ListView<InetAddress> getIPAddressList() {
        return ipList;
    }
    
    @Override
    public void exit(WindowEvent event) {
        scanner.interrupt();
    }
    
    public static class IPAddressListCell extends ListCell<InetAddress> {
        @Override
        protected void updateItem(InetAddress address, boolean empty) {
            super.updateItem(address, empty);
            if (empty || address == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(address.getHostAddress());
                MenuItem copyItm = new MenuItem("Copier");
                final ClipboardContent copyContent = new ClipboardContent();
                copyContent.putString(address.getHostAddress());
                copyItm.setOnAction(event -> Clipboard.getSystemClipboard().setContent(copyContent));
                setContextMenu(new ContextMenu(copyItm));
            }
        }
    }
}
