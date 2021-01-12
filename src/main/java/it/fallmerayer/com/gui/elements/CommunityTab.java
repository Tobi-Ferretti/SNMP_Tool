package it.fallmerayer.com.gui.elements;

import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.soulwing.snmp.Varbind;
import org.soulwing.snmp.VarbindCollection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommunityTab extends Tab {

    private final VarbindTable varbindTable;
    private final VarbindList varbindList;

    private final Map<String, Map<String, Varbind>> collections;

    public CommunityTab(String community) {
        super(community);

        this.collections = new HashMap<>();

        this.varbindTable = new VarbindTable();
        this.varbindList = new VarbindList((observable, oldValue, newValue) -> {
            if(newValue == null) {
                return;
            }
            Collection<Varbind> col = collections.get(newValue).values();
            varbindTable.setVarbinds(col);
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(varbindList, varbindTable);
        HBox.setHgrow(varbindTable, Priority.ALWAYS);

        this.setContent(hBox);
    }

    public void addResult(String ip, VarbindCollection collection) {
        Map<String, Varbind> col = new HashMap<>();
        Map<String, Varbind> oldCollection = collections.getOrDefault(ip, collection.asMap());
        Map<String, Varbind> newCollection = collection.asMap();

        for(String key : oldCollection.keySet()) {
            col.put(key, oldCollection.get(key));
        }
        for(String key : newCollection.keySet()) {
            col.put(key, newCollection.get(key));
        }

        collections.put(ip, col);

        if(!varbindList.containsIP(ip)) {
            varbindList.addIP(ip);
        } else {
            varbindList.updateIP(ip);
        }
    }
}
