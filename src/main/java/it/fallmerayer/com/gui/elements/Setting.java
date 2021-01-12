package it.fallmerayer.com.gui.elements;

import com.jfoenix.controls.JFXButton;
import it.fallmerayer.com.gui.utility.AlertUtility;
import javafx.beans.DefaultProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

@DefaultProperty("items")
public class Setting extends VBox {

    private final StringProperty title = new SimpleStringProperty();
    private final ObservableList<String> items = FXCollections.observableArrayList();


    public Setting() {
        Label title = new Label();
        title.textProperty().bind(this.title);

        ListView<String> listView = new ListView<>();
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setItems(items);
        listView.setContextMenu(getContextMenuForList(listView));

        listView.addEventFilter(MouseEvent.MOUSE_PRESSED, (e) -> {
            String inputText = null;

            if(e.getTarget() instanceof TextFieldListCell<?>) {
                TextFieldListCell<?> cell = (TextFieldListCell<?>) e.getTarget();
                inputText = cell.getText();
            } else if(e.getTarget() instanceof Text) {
                Text t = (Text)e.getTarget();
                inputText = t.getText();
            }

            if(inputText != null) {
                if(listView.getSelectionModel().getSelectedItem() != null && listView.getSelectionModel().getSelectedItem().equals(inputText)) {
                    e.consume();
                }
            }
        });

        listView.setOnEditCommit((e) -> {
            if(e.getNewValue().trim().isEmpty()) {
                e.consume();
            } else {
                int index = e.getIndex();
                List<String> listViewItems = listView.getItems();

                if (index >= 0 && index < listViewItems.size()) {
                    listViewItems.set(index, e.getNewValue());
                }
            }
        });

        HBox btns = getButtons(listView);

        getChildren().addAll(title, listView, btns);

        setSpacing(10);
    }

    public String getTitle() {
        return title.get();
    }

    private ContextMenu getContextMenuForList(ListView<String> list) {
        ContextMenu menu = new ContextMenu();

        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(new MenuItem("Add"));
        menu.getItems().add(new MenuItem("Edit"));
        menu.getItems().add(new MenuItem("Remove"));
        menu.getItems().add(new SeparatorMenuItem());

        menu.setOnAction((event) -> {
            if(event.getTarget() instanceof MenuItem) {
                MenuItem item = (MenuItem) event.getTarget();
                String text = item.getText();

                switch (text) {
                    case "Add":
                        Optional<String> result = AlertUtility.showInput(title.get(), "New Item: ");

                        result.ifPresent((res) -> {
                            if(!items.contains(res)) {
                                items.add(res);
                            } else {
                                AlertUtility.showError("Es gibt bereits einen Eintrag mit diesem Wert!");
                            }
                        });
                        break;
                    case "Edit":
                        if(list.getSelectionModel().getSelectedItem() != null) {
                            list.edit(list.getSelectionModel().getSelectedIndex());
                        } else {
                            AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
                        }
                        break;
                    case "Remove":
                        if(list.getSelectionModel().getSelectedItem() != null) {
                            items.remove(list.getSelectionModel().getSelectedItem());
                        } else {
                            AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
                        }
                        break;
                }
            }
        });

        return menu;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public ObservableList<String> getItems() {
        return items;
    }


    private HBox getButtons(ListView<String> list) {
        HBox buttons = new HBox();
        buttons.setSpacing(20);

        JFXButton addButton = new JFXButton("Hinzufügen");
        addButton.getStyleClass().add("settings-button");
        addButton.setOnAction((e) -> {
            Optional<String> result = AlertUtility.showInput(title.get(), "Neues Element: ");

            result.ifPresent((res) -> {
                if(!items.contains(res)) {
                    items.add(res);
                } else {
                    AlertUtility.showError("Es gibt bereits einen Eintrag mit diesem Wert!");
                }
            });
        });


        JFXButton editButton = new JFXButton("Bearbeiten");
        editButton.getStyleClass().add("settings-button");
        editButton.setOnAction((e) -> {
            if(list.getSelectionModel().getSelectedItem() != null) {
                list.edit(list.getSelectionModel().getSelectedIndex());
            } else {
                AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
            }
        });


        JFXButton removeButton = new JFXButton("Löschen");
        removeButton.getStyleClass().add("settings-button");
        removeButton.setOnAction((e) -> {
            if(list.getSelectionModel().getSelectedItem() != null) {
                items.remove(list.getSelectionModel().getSelectedItem());
            } else {
                AlertUtility.showInformation("Ein Eintrag muss ausgewählt sein!");
            }
        });

        buttons.getChildren().addAll(addButton, editButton, removeButton);

        return buttons;
    }




}
