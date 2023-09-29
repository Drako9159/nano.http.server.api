package com.server.http.utils.network;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {

    public List<String> ipList(){
        List<String> directions = new ArrayList<>();
        try{
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                for (InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses())
                    if (interfaceAddress.getAddress().isSiteLocalAddress()) {
                        directions.add(interfaceAddress.getAddress().getHostAddress());
                    }
            }
        } catch (SocketException e){
            e.printStackTrace();
        }
        return directions;
    }
}
