package it.fallmerayer.com.scanner;

import it.fallmerayer.com.utility.IPHelper;
import org.soulwing.snmp.*;

import java.util.List;

public class Scanner {


    //Scannt ein ganzes Netzwerk von der Start-IP bis hin zur Broadcast-IP welche mittels der Maske berechnet wird.
    public static void scanNetwork(String ip, int mask, List<String> mibs, List<String> oids, List<String> communities, boolean useGet) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        if(mask < 0 || mask > 32) {
            throw new IllegalArgumentException("Mask has to be smaller than 32 and greater than 0");
        }


        for(String address : IPHelper.calculateNetwork(ip, mask)) {
            scanIP(address, mibs, oids, communities, useGet);
        }
    }


    //Scannt das ganze Netzwerk ab der StartIP
    public static void scanNetwork(String startIP, String endIP, List<String> mibs, List<String> oids, List<String> communities, boolean useGet) {
        if(startIP == null || startIP.trim().isEmpty()) {
            throw new IllegalArgumentException("Start-IP cannot be empty!");
        }

        if(endIP == null || endIP.trim().isEmpty()) {
            throw new IllegalArgumentException("End-IP cannot be empty!");
        }


        for(String address : IPHelper.calculateNetwork(startIP, endIP)) {
            scanIP(address, mibs, oids, communities, useGet);
        }
    }

    //Scannt einzelen IP
    public static void scanIP(String ip, List<String> mibs, List<String> oids, List<String> communities, boolean useGet) {
        if(ip == null || ip.trim().isEmpty()) {
            throw new IllegalArgumentException("IP cannot be empty!");
        }

        communities = StandardSettings.getCommunities(communities);
        oids = StandardSettings.getOIDs(oids, useGet);
        Mib mib = StandardSettings.getMIB(mibs);

        SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
        target.setAddress(ip);

        SimpleSnmpTargetConfig config = new SimpleSnmpTargetConfig();
        config.setTimeout(2000);
        config.setRetries(1);

        for(String community : communities) {
            target.setCommunity(community);

            SnmpContext context = SnmpFactory.getInstance().newContext(target, mib, config, null);
            if(useGet) {
                context.asyncGet(new ResultCallback(community), oids);
            } else {
                context.asyncGetNext(new ResultCallback(community), oids);
            }
        }
    }
}
