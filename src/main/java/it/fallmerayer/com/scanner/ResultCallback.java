package it.fallmerayer.com.scanner;

import it.fallmerayer.com.gui.GUI;
import org.soulwing.snmp.SnmpCallback;
import org.soulwing.snmp.SnmpEvent;
import org.soulwing.snmp.TimeoutException;
import org.soulwing.snmp.VarbindCollection;

public class ResultCallback implements SnmpCallback<VarbindCollection> {

    private final String community;

    //Gibt Resultate in der Konsole aus
    public ResultCallback(String community) {
        this.community = community;
    }

    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> e) {
        String ip = e.getContext().getTarget().getAddress();
        try {
            VarbindCollection result = e.getResponse().get();
            GUI.getInstance().addScannerResult(community, ip, result);
        } catch (TimeoutException ignore) {
            GUI.getInstance().warning("Keine Antwort von \"" + ip + "\" auf der Community \"" + this.community + "\"");
        } finally {
            e.getContext().close();
        }
    }
}
