package com.example.config;

 
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.springframework.stereotype.Component;
@Component
public class NetworkUtils {

	  public static String getLocalIpAddress() {
	        try {
	            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
	            while (networkInterfaces.hasMoreElements()) {
	                NetworkInterface networkInterface = networkInterfaces.nextElement();
	                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
	                while (inetAddresses.hasMoreElements()) {
	                    InetAddress inetAddress = inetAddresses.nextElement();
	                    // Check if the address is neither a loopback address nor a link-local address
	                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress instanceof java.net.Inet6Address) {
	                        String address = inetAddress.getHostAddress();
	                        // Remove zone index if present
	                        int percentIndex = address.indexOf('%');
	                        if (percentIndex != -1) {
	                            address = address.substring(0, percentIndex);
	                        }
	                        return address;
	                    }
	                }
	            }
	            return "Unable to get IPv6 address";
	        } catch (SocketException e) {
	            e.printStackTrace();
	            return "Unable to get IPv6 address";
	        }
	    }
}
