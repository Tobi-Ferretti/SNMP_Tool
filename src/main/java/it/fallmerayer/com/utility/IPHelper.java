package it.fallmerayer.com.utility;

import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class IPHelper {

    //Berechnet Ip adresse des Netzwerks
    public static ArrayList<String> calculateNetwork(String ip, int mask) {
        int wildcard = 32 - mask;
        long binaryWildcard = (long) (Math.pow(2, wildcard) - 1);
        long netId = (getAsBinary(ip) >> wildcard) << wildcard;
        long broadcast = netId | binaryWildcard;

        return calculateNetwork(ip, getAsString(broadcast));
    }

    //Berechnet IP adresse in angegebener Range
    public static ArrayList<String> calculateNetwork(String startIP, String lastIP) {
        ArrayList<String> list = new ArrayList<>();

        for(int i = 0; i <= getAsBinary(lastIP) - getAsBinary(startIP); i++) {
            list.add(getAsString(getAsBinary(startIP) + i));
        }

        return list;
    }


    private static long getAsBinary(String address) {
        long binary = 0;

        String[] parts = address.split("\\.");
        for(int i = 0; i < 4; i++) {
            binary |= Long.parseLong(parts[i]) << (8 * (parts.length - i - 1));
        }

        return binary;
    }

    private static String getAsString(long binary) {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < 4; i++) {
            long block;

            block = binary & 255;
            binary >>= 8;

            builder.insert(0, block);
            builder.insert(0, '.');
        }

        builder.deleteCharAt(0);
        return builder.toString();
    }
}
