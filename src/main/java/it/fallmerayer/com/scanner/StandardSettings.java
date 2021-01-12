package it.fallmerayer.com.scanner;


import it.fallmerayer.com.gui.GUI;
import org.soulwing.snmp.Mib;
import org.soulwing.snmp.MibFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class StandardSettings {

    private static final List<String> mibs;

    static {
        mibs = new ArrayList<>();

        try {
            Reader r = new InputStreamReader(StandardSettings.class.getResourceAsStream("/mibs.txt"));
            BufferedReader reader = new BufferedReader(r);

            String line;
            while((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    mibs.add(line);
                }
            }
        } catch (IOException ignore) {}
    }

    //Gibt angegeben Communities zurück
    public static List<String> getCommunities(List<String> communities) {
        if(communities == null || communities.isEmpty()) {
            communities = new ArrayList<>();
            communities.add("public");
            communities.add("private");
        }

        return communities;
    }


    //Gibt standart MIB zurück mit custom MIBS
    public static Mib getMIB(List<String> customMibs) {
        if(customMibs == null || customMibs.isEmpty()) {
            customMibs = mibs;
        }

        Mib mib = MibFactory.getInstance().newMib();

        for(String m : customMibs) {
            try {
                mib.load(m);
            } catch (IOException e) {
                GUI.getInstance().error("Die MIB " + m + " konnte nicht geladen werden!");
            }
        }

        return mib;
    }


    //Gibt angegegebene OIDs und standart OIDs zurück
    public static List<String> getOIDs(List<String> oids, boolean useGet) {
        if(oids == null) {
            oids = new ArrayList<>();
        }

        oids.add("sysName" + (useGet ? ".0" : ""));
        oids.add("sysContact" + (useGet ? ".0" : ""));
        oids.add("sysUpTime" + (useGet ? ".0" : ""));
        oids.add("sysDescr" + (useGet ? ".0" : ""));
        oids.add("sysLocation" + (useGet ? ".0" : ""));
        oids.add("sysServices" + (useGet ? ".0" : ""));

        return oids;
    }

    //Gibt standart MiBs zurück
    public static List<String> getMIBs() {
        return mibs;
    }
}
