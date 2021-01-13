package it.fallmerayer.com.gui.elements;


import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ListView;

public class VarbindList extends JFXListView<String> {

    public VarbindList(ChangeListener<? super String> listener) {
        this.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void addIP(String ip) {
        getItems().add(ip);
    }

    public boolean containsIP(String ip) {
        return getItems().contains(ip);
    }

    public void updateIP(String ip) {
        if(getSelectionModel().getSelectedItem() != null && getSelectionModel().getSelectedItem().equals(ip)) {
            getSelectionModel().clearSelection();
            getSelectionModel().select(ip);
        }
    }
}

