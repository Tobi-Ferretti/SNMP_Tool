package it.fallmerayer.com.scanner.listener;

import it.fallmerayer.com.gui.GUI;
import it.fallmerayer.com.scanner.StandardSettings;
import org.soulwing.snmp.SnmpFactory;
import org.soulwing.snmp.SnmpListener;
import org.soulwing.snmp.SnmpNotificationEvent;
import org.soulwing.snmp.VarbindCollection;

public class SNMPListener {

    private SnmpListener listener;
    private int port;

    //Erstellt einen Listener mit dem Standardport
    public SNMPListener() {
        this(187);
    }

    //Erstellt einen Listener mit dem definierten Port
    public SNMPListener(int port) {
        this.port = port;
    }


    //Ändert den Port
    public void changePort(int port) {
        if(port < 1 || port > 65536) {
            throw new IllegalArgumentException("Port muss zwischen 1 und 65536 sein");
        }

        this.port = port;
    }

    //Startet Listener
    public void start() {
        if(listener == null) {
            listener = SnmpFactory.getInstance().newListener(port, StandardSettings.getMIB(GUI.getInstance().getSettingMIBs()));
            listener.addHandler(this::handleNotification);
            GUI.getInstance().info("Listener hört jetzt auf dem Port " + port + "!");
        }
    }

    //Stoppt Listener
    public void stop() {
        if(listener != null) {
            listener.removeHandler(this::handleNotification);
            listener.close();
            GUI.getInstance().info("Listener wurde gestoppt!");
            listener = null;
        }
    }


    private Boolean handleNotification(SnmpNotificationEvent e) {
        String ip = e.getSubject().getPeer().getAddress();
        VarbindCollection result = e.getSubject().getVarbinds();

        GUI.getInstance().info("Neuer Trap/Inform von " + ip + "!");

        GUI.getInstance().addListenerResult("Results", ip, result);

        return true;
    }
}
