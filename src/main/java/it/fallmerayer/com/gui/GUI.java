package it.fallmerayer.com.gui;

import it.fallmerayer.com.gui.elements.CommunityTab;
import it.fallmerayer.com.gui.elements.IPField;
import it.fallmerayer.com.scanner.Scanner;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import org.soulwing.snmp.VarbindCollection;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GUI implements Initializable {

    public Button btn_Scan;
    public ComboBox<String> combo_IP;
    public IPField field_startAddress;
    public IPField field_endAddress;
    public TabPane tab_results;
    public ComboBox<CheckBox> combo_Community;
    public ComboBox<String> combo_Method;

    private final Map<String, CommunityTab> map_Tabs = new HashMap<>();
    private static GUI INSTANCE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_IP.getItems().setAll("Host", "Network", "Custom-Range");
        combo_IP.getSelectionModel().select(0);

        CheckBox cb = new CheckBox("Public");
        cb.setSelected(true);
        combo_Community.getItems().setAll(cb, new CheckBox("Private"));
        combo_Community.getSelectionModel().select(0);

        combo_Method.getItems().addAll( "GetNext", "Get");
        combo_Method.getSelectionModel().select(0);

        INSTANCE = this;
    }


    public void on_ComboIP_Change() {
        String selected = combo_IP.getSelectionModel().getSelectedItem();
        field_startAddress.setMaskDisabled(!selected.equalsIgnoreCase("Network"));
        field_endAddress.setDisable(!selected.equalsIgnoreCase("Custom-Range"));

        field_endAddress.clear();
        field_startAddress.clear();
    }

    public void on_Scan_Click() {
        String firstIP = field_startAddress.getIP();
        int mask = field_startAddress.getMask();
        String endIP = field_endAddress.getIP();
        boolean useGet = combo_Method.getSelectionModel().getSelectedItem().equalsIgnoreCase("Get");

        if(endIP == null && mask == -1) {
            System.out.println("Scanning Host " + firstIP);
            Scanner.scanIP(firstIP, null, null, null, useGet);
        } else if(endIP == null) {
            System.out.println("Scanning Network " + firstIP + "/" + mask);
            Scanner.scanNetwork(firstIP, mask, null, null, null, useGet);
        } else {
            System.out.println("Scanning Range " + firstIP + " - " + endIP);
            Scanner.scanNetwork(firstIP, endIP, null, null, null, useGet);
        }
    }

    public void addResult(String community, String ip, VarbindCollection collection) {
        Platform.runLater(() -> {
            if(map_Tabs.size() != tab_results.getTabs().size()) {
                tab_results.getTabs().clear();
                map_Tabs.clear();
            }

            CommunityTab tab = map_Tabs.getOrDefault(community, new CommunityTab(community));
            tab.addResult(ip, collection);

            if(!map_Tabs.containsKey(community)) {
                map_Tabs.put(community, tab);
                tab_results.getTabs().add(tab);
            }
        });
    }

    public static GUI getInstance() {
        return INSTANCE;
    }
}
