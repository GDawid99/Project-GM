package com.project.files.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class GenerateNamesOfBoxes {
    private Properties properties;
    private FileInputStream fileInputStream;
    private final String filename;

    private final List<String> listGenerateSeedsMethod;
    private final List<String> listNeighbourhood;
    private final List<String> listBoundaryConditions;


    public GenerateNamesOfBoxes(String filename) {
        this.filename = filename;
        properties = new Properties();
        listGenerateSeedsMethod = new LinkedList<>();
        listNeighbourhood = new LinkedList<>();
        listBoundaryConditions = new LinkedList<>();
    }


    public void setDatafromFile() {
        try {
            fileInputStream = new FileInputStream(filename);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        listGenerateSeedsMethod.add(properties.getProperty("textGenSeedRandom"));
        listGenerateSeedsMethod.add(properties.getProperty("textGenSeedClick"));
        listGenerateSeedsMethod.add(properties.getProperty("textGenSeedRegular"));

        listNeighbourhood.add(properties.getProperty("textNbhVN"));
        listNeighbourhood.add(properties.getProperty("textNbhM"));
        listNeighbourhood.add(properties.getProperty("textNbhPR"));
        listNeighbourhood.add(properties.getProperty("textNbhHR"));

        listBoundaryConditions.add(properties.getProperty("textBoundaryPer"));
        listBoundaryConditions.add(properties.getProperty("textBoundaryAbs"));
    }

    public void setAllValuesGenSeeds(ChoiceBox<String> ch, ObservableList<String> observableList) {
        setVisibleValueGenSeeds(ch);
        observableList = FXCollections.observableArrayList(listGenerateSeedsMethod);
        ch.setItems(observableList);
    }

    public void setAllValuesNeighbourhood(ChoiceBox<String> ch, ObservableList<String> observableList) {
        setVisibleValueNeighbourhood(ch);
        observableList = FXCollections.observableArrayList(listNeighbourhood);
        ch.setItems(observableList);
    }

    public void setAllValuesBoundaryConditions(ChoiceBox<String> ch, ObservableList<String> observableList) {
        setVisibleValueBoundaryConditions(ch);
        observableList = FXCollections.observableArrayList(listBoundaryConditions);
        ch.setItems(observableList);
    }

    private void setVisibleValueGenSeeds(ChoiceBox<String> ch) {
        ch.setValue(listGenerateSeedsMethod.get(0));
    }

    private void setVisibleValueNeighbourhood(ChoiceBox<String> ch) {
        ch.setValue(listNeighbourhood.get(0));
    }

    private void setVisibleValueBoundaryConditions(ChoiceBox<String> ch) {
        ch.setValue(listBoundaryConditions.get(0));
    }

    public List<String> getListGenerateSeedsMethod() {
        return listGenerateSeedsMethod;
    }

    public List<String> getListNeighbourhood() {
        return listNeighbourhood;
    }

    public List<String> getListBoundaryConditions() {
        return listBoundaryConditions;
    }
}
