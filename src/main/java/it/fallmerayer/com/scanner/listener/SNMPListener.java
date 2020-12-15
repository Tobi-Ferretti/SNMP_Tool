package it.fallmerayer.com.scanner.listener;

import it.fallmerayer.com.scanner.StandardSettings;
import org.soulwing.snmp.SnmpFactory;
import org.soulwing.snmp.SnmpListener;
import org.soulwing.snmp.SnmpNotificationEvent;

public class SNMPListener {

    private SnmpListener listener;

    //Erstellt einen Listener mit dem Standardport
    public SNMPListener() {
        listener = SnmpFactory.getInstance().newListener(StandardSettings.getMIB());
    }

    //Erstellt einen Listener mit dem definierten Port
    public SNMPListener(int port) {
        changePort(port);
    }


    //Ändert den Port
    public void changePort(int port) {
        if(port < 1024 || port > 65536) {
            throw new IllegalArgumentException("Port has to be greater than 1024 and less than 65536");
        }

        listener = SnmpFactory.getInstance().newListener(port, StandardSettings.getMIB());
    }

    //Startet Listener
    public void start() {
        if(listener != null) {
            System.out.println("Listener started");
            listener.addHandler(this::handleNotification);
        }
    }

    //Stoppt Listener
    public void stop() {
        if(listener != null) {
            System.out.println("Listener stopped!");
            listener.close();
        }
    }


    private Boolean handleNotification(SnmpNotificationEvent e) {
        System.out.println("New Trap/Inform: " + e);
        return true;
    }
}
