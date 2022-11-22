package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class VesselManagerApplication extends Application {
    public BorderPane setup() throws SQLException {
        ArrayList<String> PortList = DatabaseQueries.getAllPortName();
        ArrayList<Vessel> AvailableVesselList = new ArrayList<Vessel>();

        Label lab1 = new Label("From port");
        ComboBox<String> combo1 = new ComboBox<>();
        for (int i = 0; i < PortList.size(); i++)
            combo1.getItems().add(PortList.get(i));
        lab1.setPrefSize(150,30);

        Label lab2 = new Label("To port");
        ComboBox<String> combo2 = new ComboBox<>();
        for (int i = 0; i < PortList.size(); i++)
            combo2.getItems().add(PortList.get(i));
        lab2.setPrefSize(150,30);

        Label lab3 = new Label("number of containers to add");
        lab3.setPrefSize(150,30);
        TextField fld = new TextField();

        Label lab4 = new Label("select vessel and\nsend containers");
        ComboBox<String> combo3 = new ComboBox<>();
        lab4.setPrefSize(150,30);

        TextArea res = new TextArea();
        res.setStyle( "-fx-font: 16 arial;");

        Button srch = new Button("Search");
        srch.setOnAction(e -> {
                AvailableVesselList.clear();
                combo3.getItems().clear();
                int cargo = 0;
                if (fld.getText().matches("^[0-9]*$+"))
                    cargo = Integer.parseInt(fld.getText());
                try {
                    ArrayList<Vessel> VesselList = DatabaseQueries.getAvailableVessel(combo1.getValue(), combo2.getValue(), cargo);
                    StringBuilder message = new StringBuilder("Available vessel(s) :\n");
                    for (int i = 0; i < VesselList.size(); i++) {
                        message.append("- " + VesselList.get(i).name + "\n\tcargo : " + Integer.toString(VesselList.get(i).cargo) + "\n\tmax capacity : " + Integer.toString(VesselList.get(i).capacity));
                        AvailableVesselList.add(VesselList.get(i));
                    }
                    for (int i = 0; i < AvailableVesselList.size(); i++) // set available vessel dropdown list items
                        combo3.getItems().add(AvailableVesselList.get(i).name);
                    res.setText(String.valueOf(message));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        );
        srch.setStyle(
                "-fx-font: 22 arial; -fx-base: rgb(16,156,3); " +
                "-fx-text-fill: rgb(255,255,255);");

        Button vldt = new Button("Send");
        vldt.setOnAction(e -> {
                int cargo = 0;
                if (fld.getText().matches("^[0-9]*$"))
                    cargo = Integer.parseInt(fld.getText());
                try {
                    DatabaseQueries.addContainerToVessel(combo3.getValue(), cargo);
                    res.setText(String.valueOf("Added " + Integer.toString(cargo) + " of cargo to " + combo3.getValue()));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        );
        vldt.setStyle(
                "-fx-font: 22 arial; -fx-base: rgb(3,57,156); " +
                        "-fx-text-fill: rgb(255,255,255);");

        GridPane pane1 = new GridPane();
        pane1.add(lab1,1,1);
        pane1.add(combo1,1,2);
        pane1.add(lab2,2,1);
        pane1.add(combo2,2,2);
        pane1.add(lab3,3,1);
        pane1.add(fld,3,2);

        GridPane pane2 = new GridPane();
        pane2.add(srch,1, 2);
        pane2.add(lab4, 1, 4);
        pane2.add(combo3, 1, 5);
        pane2.add(vldt, 2, 5);

        BorderPane root = new BorderPane();
        root.setTop(pane1);
        root.setCenter(pane2);
        root.setBottom(res);

        return root;
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Scene scene = new Scene(setup(), 500, 500);
        stage.setTitle("Vessel Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}