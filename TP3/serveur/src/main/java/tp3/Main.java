package tp3;

import tp3.io.SSLConnectionManager;
import tp3.io.SSLConnectionManager.SSLConnectionListener;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws Exception {
		SSLConnectionListener listener = SSLConnectionManager.openSocket();
		listener.addHandler(sslSocket -> {
			System.out.println("Connection established!");
			System.out.println("\t> " + sslSocket);
		});
		listener.run();
		System.out.println("Press any key to continue");
		new Scanner(System.in).nextLine();
	}
}
