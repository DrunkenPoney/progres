package tp1.models;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.apache.commons.lang.StringUtils.repeat;
import static org.apache.commons.lang.StringUtils.rightPad;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tp1.models.Utils.split;

class NetworkScannerTest {
    private NetworkScanner   scanner;
    private SubnetUtils      subnetUtils;
    private InterfaceAddress address;
    
    @BeforeEach
    void setUp() throws SocketException {
        scanner = new NetworkScanner();
        
        
        NetworkInterface netInterface = NetworkInterface.networkInterfaces()
                                                        .filter(ni -> {
                                                            boolean accept = false;
                                                            try {
                                                                accept = ni.isUp() && !ni.isLoopback() && !ni.isVirtual();
                                                            } catch (SocketException e) {
                                                                e.printStackTrace();
                                                            }
                                                            return accept;
                                                        })
                                                        .findFirst()
                                                        .orElse(null);
        
        assert netInterface != null;
        address = netInterface.getInterfaceAddresses()
                              .stream()
                              .filter(addr -> addr.getAddress() instanceof Inet4Address)
                              .findAny()
                              .orElse(null);
        
        assert address != null;
        String mask = stream(split(rightPad(repeat("1", address.getNetworkPrefixLength()), 32, '0'), 8))
                              .map(str -> Integer.valueOf(str, 2))
                              .map(Objects::toString)
                              .collect(Collectors.joining("."));
        
        subnetUtils = new SubnetUtils(address.getAddress().getHostAddress(), mask);
    }
    
    @AfterEach
    void tearDown() {
        scanner.interrupt();
    }
    
    @Test
    void scanNetwork() {
        subnetUtils.setInclusiveHostCount(true);
        String[] addresses = subnetUtils.getInfo().getAllAddresses();
        
        assertTrue(scanner.scanNetwork(address)
                          .map(InetAddress::getHostAddress)
                          .map(Objects::toString)
                          .allMatch(addr -> ArrayUtils.contains(addresses, addr)));
    }
}