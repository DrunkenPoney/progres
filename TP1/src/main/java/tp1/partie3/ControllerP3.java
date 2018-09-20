package tp1.partie3;

import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import tp1.IBaseContoller;
import tp1.models.PortScanner;
import tp1.models.Utils;
import tp1.models.ui.ColoredText;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tp1.models.Utils.REGEX_URL_PARTS;
import static tp1.models.Utils.toggleStyleClass;

public class ControllerP3 extends VBox implements IBaseContoller {
    private static final double                         LIST_CELL_HEIGHT = 24;
    private final        ChangeListener<Number>         progressListener;
    
    @FXML
    protected JFXProgressBar progressBar;
    
    @FXML
    protected Button btnDomainAddress;
    
    @FXML
    protected TextField domainName, domainAddress;
    
    @FXML
    protected ListView<PortScanResult> portsList;
    
    @FXML
    protected GridPane grid;
    
    private PortScanner scanner;
    
    public ControllerP3() throws IOException {
        scanner = null;
        progressListener = (observable, oldValue, newValue) -> {
            synchronized (this) {
                double progress = newValue.doubleValue() / PortScanner.NUMBER_OF_PORTS;
                Platform.runLater(() -> {
                    domainAddress.setText(
                            String.format("Scanning ports...    %d/%d (%d %%)",
                                          newValue.intValue(),
                                          PortScanner.NUMBER_OF_PORTS,
                                          Math.round(progress * 100)));
                    progressBar.setProgress(progress);
                });
            }
        };
        
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setRoot(this);
        loader.load(getClass().getResourceAsStream("/Partie3.fxml"));
        
        this.getStylesheets().add(getClass().getResource("/styles.css").toString());
    }
    
    @FXML
    private void initialize() {
        portsList.setMinHeight(0);
        portsList.prefHeightProperty()
                 .bind(Bindings.createDoubleBinding(
                         () -> portsList.getItems().size() > 0
                               ? portsList.getItems().size() * LIST_CELL_HEIGHT + 2 : 0,
                         Bindings.size(portsList.getItems())));
        
        portsList.maxHeightProperty()
                 .bind(Bindings.when(Bindings.equal(Bindings.size(portsList.getItems()), 0))
                               .then(0)
                               .otherwise(Double.MAX_VALUE));
        
        portsList.setCellFactory(param -> new ListCell<>() {
            
            @Override
            protected void updateItem(PortScanResult item, boolean empty) {
                super.updateItem(item, empty);
                Platform.runLater(() -> {
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        ColoredText text = new ColoredText();
                        text.appendText("Port: ")
                            .appendText(String.valueOf(item.addr.getPort()), Color.DARKGOLDENROD)
                            .appendText(", Temps de réponse: ")
                            .appendText(String.valueOf(item.responseTime.toMillis()), Color.DARKCYAN)
                            .appendText(" ms");
                        setGraphic(text);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    }
                });
            }
        });
    }
    
    public void scanAddress(InetAddress address) {
        domainName.setText(address.getHostAddress());
        try {
            getDomainAddress();
        } catch (PortScanner.ScanException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void getDomainAddress() throws PortScanner.ScanException {
        domainName.setText(domainName.getText().trim());
        Matcher     matcher = Pattern.compile(REGEX_URL_PARTS).matcher(domainName.getText());
        InetAddress iaddr;
        String      domain;
        domainName.getStyleClass().remove(Utils.ERROR_STYLE_CLASS);
        
        if (matcher.matches()) {
            try {
                portsList.getItems().clear();
                domainName.setText(domain = matcher.group(2));
                domainAddress.setText("Scanning ports...    0/" + PortScanner.NUMBER_OF_PORTS + " (0%)");
                
                if (scanner != null) { scanner.interrupt(); }
                iaddr = InetAddress.getByName(domain);
                scanner = new PortScanner(iaddr);
                scanner.getProgressProperty().addListener(progressListener);
                progressBar.setVisible(true);
                scanner.scan((final InetSocketAddress socketAddress, final Duration responseTime) -> {
                    portsList.getItems().add(new PortScanResult(socketAddress, responseTime, true));
                    return true;
                }, () -> {
                    scanner.getProgressProperty().removeListener(progressListener);
                    progressBar.setVisible(false);
                    Platform.runLater(() -> domainAddress.setText("IP: " + iaddr.getHostAddress()));
                });
                
            } catch (UnknownHostException e) {
                printInvalidMsg("L'adresse spécifiée n'existe pas");
            }
        } else {
            printInvalidMsg("L'adresse spécifiée est invalide");
        }
    }
    
    private void printInvalidMsg(String msg) {
        toggleStyleClass(domainName, Utils.ERROR_STYLE_CLASS, true);
        
        domainAddress.setText(msg);
    }
    
    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (event.getSource() == domainName) {
            if (event.getCode().isLetterKey()) { domainName.getStyleClass().remove(Utils.ERROR_STYLE_CLASS); }
            if (event.getCode() == KeyCode.ENTER) { btnDomainAddress.fire(); }
        } else if (event.getSource() instanceof Button) {
            ((Button) event.getSource()).fire();
        }
    }
    
    @Override
    @SuppressWarnings("unused")
    public void exit(WindowEvent event) {
        if (scanner != null) { scanner.interrupt(); }
    }
    
    private static class PortScanResult {
        public final InetSocketAddress addr;
        public final Duration          responseTime;
        public final boolean           open;
        
        private PortScanResult(InetSocketAddress addr, Duration responseTime, boolean open) {
            this.addr = addr;
            this.responseTime = responseTime;
            this.open = open;
        }
    }
}
