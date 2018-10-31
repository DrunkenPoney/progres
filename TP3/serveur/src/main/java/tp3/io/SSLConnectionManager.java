package tp3.io;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static tp3.Constants.*;
import static tp3.utils.Utils.randomLocalSocket;

public class SSLConnectionManager {
	private static final SSLContext SSL_CONTEXT;
	
	static {
		try {
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			keyStore.load(KEYSTORE.openStream(), KEYSTORE_PASSWORD);
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(keyStore);
			
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, KEYSTORE_PASSWORD);
			
			System.setProperty("https.protocols", "SSL");
			
			SSL_CONTEXT = SSLContext.getInstance("SSL");
			SSL_CONTEXT.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Nullable
	public static SSLConnectionListener openSocket() {
		try {
			SSLServerSocket socket = (SSLServerSocket)
					SSL_CONTEXT.getServerSocketFactory()
					           .createServerSocket(randomLocalSocket(MIN_PORT, MAX_PORT).getPort());
			socket.setNeedClientAuth(true);
			return new SSLConnectionListener(socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static class SSLConnectionListener implements Runnable {
		private final    SSLServerSocket           socket;
		private final    Set<SSLSocket>            connections;
		private final    List<Consumer<SSLSocket>> handlers;
		private final    ExecutorService           executor = Executors.newSingleThreadExecutor();
		private volatile boolean                   running  = false;
		
		private SSLConnectionListener(@NotNull SSLServerSocket socket) {
			this.connections = new HashSet<>();
			this.handlers = new ArrayList<>();
			this.socket = socket;
		}
		
		public SSLServerSocket getSocket() {
			return socket;
		}
		
		public Set<SSLSocket> getConnections() {
			connections.removeIf(Socket::isClosed);
			return connections;
		}
		
		@Override
		public void run() {
			running = true;
			while (running) {
				try {
					final SSLSocket client = (SSLSocket) this.socket.accept();
					Flowable.fromIterable(handlers)
					        .subscribeOn(Schedulers.from(executor))
					        .doOnNext(handler -> handler.accept(client))
					        .subscribe();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public boolean isRunning() {
			return running;
		}
		
		public void stop() {
			running = false;
		}
		
		public void kill(long timeout) {
			stop();
			try {
				executor.shutdown();
				if (!executor.awaitTermination(max(timeout, 0), TimeUnit.MILLISECONDS))
					executor.shutdownNow();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public boolean addHandler(Consumer<SSLSocket> handler) {
			return this.handlers.add(handler);
		}
		
		public boolean removeHandler(Consumer<SSLSocket> handler) {
			return this.handlers.remove(handler);
		}
	}
}
