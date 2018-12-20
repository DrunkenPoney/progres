package tp3.model;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.READ;

public class Transmission {
	public static void bob() throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//		ks.load(Transmission.class.getResourceAsStream("/private_pkcs12.jks"), "progres".toCharArray());
		ks.load(Files.newInputStream(Paths.get("public.jks"), READ), "progres".toCharArray());
		
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, "progres".toCharArray());
		
		System.setProperty("https.protocols", "SSL");
		
		SSLContext ctxt = SSLContext.getInstance("SSL");
		ctxt.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
		
		SSLSocket socket = (SSLSocket) ctxt.getSocketFactory().createSocket();
//		socket.setUseClientMode(true);
		socket.setNeedClientAuth(true);
		socket.setUseClientMode(true);
		socket.addHandshakeCompletedListener(ev -> {
			System.out.println(ev.getSession());
			System.out.println(ev.getCipherSuite());
			System.out.println(Arrays.toString(ev.getLocalCertificates()));
			System.out.println(ev.getLocalPrincipal());
			System.out.println(ev.getSocket());
		});
		socket.connect(new InetSocketAddress(8443));
		socket.startHandshake();
//		socket.startHandshake();
		
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
		bw.write("LOGIN:{ 'username':'admin', 'password': 'admin' }".replace('\'', '"'));
		bw.flush();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
		String[] response = br.lines().collect(Collectors.joining("\n")).split(":", 2);
		System.out.printf("\nIs successful? %b\n\tResponse: %s", response[0].equals("1"), response[1]);
	}
}
