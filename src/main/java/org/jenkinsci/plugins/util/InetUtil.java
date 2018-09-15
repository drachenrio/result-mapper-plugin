package org.jenkinsci.plugins.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class InetUtil {

    @SuppressWarnings("rawtypes")
    public static String getHostIpAddress() {
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e.nextElement();
                Enumeration ia = ni.getInetAddresses();
                while (ia.hasMoreElements()) {
                    InetAddress i = (InetAddress) ia.nextElement();
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(i.getHostAddress());
                }
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }
}
