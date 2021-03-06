package tp2.models.utils;

import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;

public final class Constants {
//	public static final String MONGODB_CONNECTION_STRING = "mongodb+srv://mongo-user:TgR5Wsxk7aFuU88r@cluster0-p2itp.mongodb.net/?retryWrites=true";
	
	public static final String MONGODB_CONNECTION_STRING = "mongodb://mongo-user:TgR5Wsxk7aFuU88r@cluster0-shard-00-00-p2itp.mongodb.net:27017,cluster0-shard-00-01-p2itp.mongodb.net:27017,cluster0-shard-00-02-p2itp.mongodb.net:27017/?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";
//	public static final String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";
	
	public static final String DATABASE          = "tp2";
	public static final String DOCUMENTS_PACKAGE = "tp2.models.db.documents";
	
	public static final int BUFFER_SIZE           = 1024;
	public static final int MIN_PORT_NUMBER       = 0;
	public static final int MAX_PORT_NUMBER       = 65535;
	
	public static final String I18N_MESSAGES_BUNDLE_BASE_NAME = "i18n.messages";
	
	
	//////////////////////////////////////// GUI ////////////////////////////////////////
	public static final int      LEFT_PANEL_WIDTH         = 250;
	public static final Duration WIDTH_ANIMATION_DURATION = Duration.millis(350);
	
	///////////////////////////////////// RESOURCES /////////////////////////////////////
	public static final Resource RSS_FXML_MAIN       = new Resource("/main.fxml");
	public static final Resource RSS_FXML_LEFT_PANEL = new Resource("/left-panel.fxml");
	public static final Resource RSS_CSS_MAIN        = new Resource("/css/main.css");
	
	public static class Resource {
		private final String path;
		
		private Resource(String path) {
			this.path = path;
		}
		
		public InputStream getStream() {
			return getClass().getResourceAsStream(path);
		}
		
		public URL get() {
			return getClass().getResource(path);
		}
		
		public String getPath() {
			return path;
		}
	}
}
