package tp1.partie1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import tp1.IBaseContoller;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ControllerP1 extends GridPane implements IBaseContoller {
    
    @FXML
    protected TextField networkDeviceIP, localhostIP, name, fullname, publicIP;
    
    public ControllerP1() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setRoot(this);
        loader.load(getClass().getResourceAsStream("/Partie1.fxml"));
        getStylesheets().add(getClass().getResource("/styles.css").toString());
    }
    
    @FXML
    private void initialize() throws IOException {
        Inet4Address localhost = (Inet4Address) InetAddress.getLocalHost();
        name.setText(localhost.getHostName());
        fullname.setText(localhost.getCanonicalHostName());
        localhostIP.setText(InetAddress.getLoopbackAddress().toString());
        networkDeviceIP.setText(InetAddress.getLocalHost().getHostAddress());
    
        publicIP.setText(new Scanner(new URL("https://api.ipify.org").openStream(), StandardCharsets.UTF_8)
                                 .useDelimiter("\\A").next());
    }
    
    @Override
    public void exit(WindowEvent event) {}
}
