package tp3;

import java.net.URL;

public final class Constants {
	public static final String MONGODB_CONNECTION_STRING = "mongodb://mongo-user:TgR5Wsxk7aFuU88r@cluster0-shard-00-00-p2itp.mongodb.net:27017,cluster0-shard-00-01-p2itp.mongodb.net:27017,cluster0-shard-00-02-p2itp.mongodb.net:27017/?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin";
	public static final String DOCUMENTS_PACKAGE         = "tp3.bd.entities";
	public static final String DATABASE                  = "tp3";
	
	public static final String DEFAULT_REGION_CODE       = "fr_CA";
	
//	public static final URL KEYSTORE = Constants.class.getResource("/private_pkcs12.jks");
//	public static final URL KEYSTORE = Constants.class.getResource("/");
	public static final char[] KEYSTORE_PASSWORD = "progres".toCharArray();
	public static final int SSL_PORT = 8443;
	public static final int MIN_PORT = 0;
	public static final int MAX_PORT = 65535;
}
