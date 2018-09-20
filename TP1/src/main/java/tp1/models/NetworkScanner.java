package tp1.models;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NetworkScanner {
    private static final int REACHABLE_TIMEOUT = 500, MAX_THREAD_COUNT = 1000;
    private final transient ObservableList<InetAddress> addressList = FXCollections.observableArrayList();
    private                 ExecutorService             executor;
    
    /**
     * Démarre le scan du ou des réseaux
     */
    public void scan() throws SocketException {
        interrupt();
        addressList.clear();
        List<Runnable> tasks = getLocalAddresses().flatMap(this::scanNetwork)
                                                  .map(ip -> (Runnable) () -> {
                                                      try {
                                                          if (ip.isReachable(REACHABLE_TIMEOUT)) {
                                                              Platform.runLater(() -> addressList.add(ip));
                                                          }
                                                      } catch (IOException e) {
                                                          e.printStackTrace();
                                                      }
                                                  })
                                                  .collect(Collectors.toList());
        executor = Executors.newFixedThreadPool(tasks.size() < MAX_THREAD_COUNT
                                                ? tasks.size() : MAX_THREAD_COUNT);
        tasks.forEach(executor::execute);
    }
    
    /**
     * Interrompt le scan s'il y en a un en cours
     */
    public void interrupt() {
        if (this.executor != null && !this.executor.isShutdown()) {
            this.executor.shutdownNow();
        }
    }
    
    /**
     * Récupère les adresses des interfaces réseaux (ou cartes réseaux) qui
     * sont utilisées, qui ne correspondent pas à l'interface de boucle locale
     * et qui ne sont pas des interfaces virtuelles.
     *
     * @return Un {@link Stream} d'adresses d'interface réseau
     */
    private Stream<InterfaceAddress> getLocalAddresses() throws SocketException {
        return NetworkInterface.networkInterfaces()
                               .filter(ni -> {
                                   boolean accept = false;
                                   try {
                                       accept = ni.isUp() && !ni.isLoopback() && !ni.isVirtual();
                                   } catch (SocketException e) { e.printStackTrace(); }
                                   return accept;
                               })
                               .flatMap(ni -> ni.getInterfaceAddresses().stream())
                               .filter(ia -> ia.getAddress().isSiteLocalAddress());
    }
    
    /**
     * Récupère TOUTES les adresses pouvant faire parties du réseau auquel
     * appartient l'adresse passée en paramètre.
     *
     * Retourne un stream de {@link Runnable} qui testent l'accessibilité de
     * ces adresses. (ping)
     *
     * @param iaddr
     *         - L'adresse d'une interface réseau (carte réseau)
     * @return Stream de {@link Runnable}
     */
    @Contract("null -> fail")
    protected Stream<InetAddress> scanNetwork(InterfaceAddress iaddr) {
        final int        suffixLength    = 32 - iaddr.getNetworkPrefixLength();
        Set<InetAddress> ipList          = new HashSet<>();
        byte[]           addrClone, addr = new byte[4];
        
        String binaryAddr = Stream.of(iaddr.getAddress().getHostAddress().split("\\."))
                                  .map(Integer::parseInt)
                                  // Transforme l'adresse IP en binaire
                                  .map(i -> StringUtils.leftPad(Integer.toString(i, 2), 8, '0'))
                                  .collect(Collectors.joining(""))
                                  // Remplace les bits de la fin par des «0». Le nombre de
                                  // bits remplacés dépend du masque de sous-réseau
                                  .replaceAll(String.format("\\d{%d}$", suffixLength),
                                              StringUtils.repeat("0", suffixLength));
        
        // Transforme l'adresse binaire en array d'octets
        for (int i = 8; i <= binaryAddr.length(); i += 8) {
            addr[(i / 8) - 1] = Integer.valueOf(binaryAddr.substring(i - 8, i), 2).byteValue();
        }
        
        /* 2 ^ suffix = Valeur numérique du suffixe de la plus grande adresse
         *              IP pouvant faire partie du sous-réseau */
        for (int j, i = j = (int) Math.pow(2, suffixLength); i > 0; j = --i) {
            // j est une copie de i, pour éviter que i soit modifié
            
            // Il ne faut pas modifier addr, donc...
            addrClone = Arrays.copyOf(addr, addr.length);
            
            // Ajoute j à la valeur numérique de addr (octet par octet)
            if (j > 0xFFFFFF) {
                addrClone[0] = (byte) (Byte.toUnsignedInt(addr[0]) + (j / 0xFFFFFF));
                j %= 0xFFFFFF;
            }
            if (j > 0xFFFF) {
                addrClone[1] = (byte) (Byte.toUnsignedInt(addr[1]) + (j / 0xFFFF));
                j %= 0xFFFF;
            }
            if (j > 0xFF) {
                addrClone[2] = (byte) (Byte.toUnsignedInt(addr[2]) + (j / 0xFF));
                j %= 0xFF;
            }
            addrClone[3] = (byte) (Byte.toUnsignedInt(addr[3]) + j);
            
            try {
                // Ajoute l'adresse créée à la liste
                ipList.add(InetAddress.getByAddress(addrClone));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        
        return ipList.stream()
                     // Filtre la passerelle par défaut
                     .filter(ip -> !ip.getHostAddress().endsWith(".1"));
    }
    
    public ObservableList<InetAddress> getAddressList() {
        return addressList;
    }
}
