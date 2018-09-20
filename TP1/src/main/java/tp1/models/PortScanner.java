package tp1.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class PortScanner {
    public static final int    MAX_PORT_NUMBER          = 65535;
    public static final int    NUMBER_OF_PORTS          = MAX_PORT_NUMBER + 1;
    public static final int    MAX_SIMULTANEOUS_THREADS = 800;
    
    private final InetAddress     iaddr;
    private final IntegerProperty portsScanned;
    private final ExecutorService executor;
    private       boolean         isScanning;
    
    public PortScanner(@NotNull InetAddress iaddr) {
        this.iaddr = iaddr;
        this.isScanning = false;
        this.portsScanned = new SimpleIntegerProperty(0);
        this.executor = Executors.newFixedThreadPool(MAX_SIMULTANEOUS_THREADS);
    }
    
    public void scan(@NotNull ReachabilityCallback onReachable, @Nullable Runnable onScanCompletion)
            throws ScanException {
        scan(onReachable, (socketAddress, duration) -> true, onScanCompletion);
    }
    
    public void scan(@NotNull ReachabilityCallback onReachable, @NotNull ReachabilityCallback onUnreachable,
                     @Nullable Runnable onScanCompletion) throws ScanException {
        if (isScanning) { throw new ScanException(); }
        
        isScanning = true;
        portsScanned.setValue(0);
        
        for (int i = MAX_PORT_NUMBER; i >= 0 && isScanning; i--) {
            executor.submit(new Scanner((socketAddress, reachable, duration) -> {
                synchronized (this) {
                    portsScanned.setValue(portsScanned.get() + 1);
                    boolean keepScanning = reachable ? onReachable.call(socketAddress, duration)
                                                     : onUnreachable.call(socketAddress, duration);
                    if (!keepScanning || portsScanned.get() == NUMBER_OF_PORTS) {
                        interrupt();
                        if (onScanCompletion != null) { onScanCompletion.run(); }
                    }
                }
            }, i));
        }
    }
    
    public synchronized void interrupt() {
        if (isScanning) {
            executor.shutdownNow();
            isScanning = false;
        }
    }
    
    public InetAddress getAddress() {
        return iaddr;
    }
    
    public IntegerProperty getProgressProperty() {
        return portsScanned;
    }
    
    public boolean isScanning() {
        return isScanning;
    }
    
    private interface ScannerCallback {
        void call(InetSocketAddress socketAddress, boolean reachable, Duration responseTime);
    }
    
    public interface ReachabilityCallback {
        boolean call(InetSocketAddress socketAddress, Duration responseTime);
    }
    
    private class Scanner implements Runnable {
        private static final int             TIMEOUT = 350;
        private final        ScannerCallback callback;
        private final        int             port;
        
        private Scanner(@NotNull ScannerCallback callback, int port) {
            this.callback = callback;
            this.port = port;
        }
        
        @Override
        public void run() {
            InetSocketAddress socketAddress = new InetSocketAddress(iaddr, port);
            Instant startTime, endTime;
            try (Socket socket = new Socket()) {
                startTime = Instant.now();
                socket.connect(socketAddress, TIMEOUT);
                endTime = Instant.now();
                socket.close();
                callback.call(socketAddress, true, Duration.between(startTime, endTime));
            } catch (IOException ignored) {
                callback.call(socketAddress, false, Duration.of(TIMEOUT, ChronoUnit.MILLIS));
            }
        }
    }
    
    public static class ScanException extends Exception {
        public ScanException() {
            super("A scan is already in progress");
        }
    }
}
