package com.project.files;

import com.project.files.gui.GenerateNamesOfBoxes;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable{

    CanvasController canvasController;
    Alert alertError;
    GenerateNamesOfBoxes generateNamesOfBoxes;
    String filename = "src/main/resources/com/project/files/elements.properties";
    Stage stage;


    @FXML
    private ObservableList<String> observableList;


    @FXML
    private Label textSize,
            textX,
            textGenerateSeed,
            textAmountOfSeeds,
            textTypeOfModelling,
            textAmountOfIterationMC,
            textJ,
            textkt,
            textIterationMC,
            textAmountOfNewSeeds,
            textXX,
            textYY;

    @FXML
    private Button buttonGenerateArea,
            buttonGenerateSeeds,
            buttonStart,
            buttonStop,
            buttonMC,
            buttonMicroEnergy,
            buttonRecrystallization,
            buttonSaveFile,
            buttonLoadFile;

    @FXML
    private TextField tfSizeWidth,
            tfSizeHeight,
            tfAmountOfSeeds,
            tfIterationMC,
            tfJ,
            tfkt,
            tfAmountOfNewSeeds,
            tfXX,
            tfYY;

    @FXML
    private ChoiceBox<String> choiceSeedGenerator,
            choiceTypeOfModelling,
            choiceBoundaryConditions;


    @FXML private GridPane gridPane;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;

    //Method generates area
    @FXML
    protected void eventGenerateArea() {
        int w, h;
        try {
            w = Integer.parseInt(tfSizeWidth.getText());
            h = Integer.parseInt(tfSizeHeight.getText());
        } catch (NumberFormatException e) {
            alertError.setContentText("Nie podano liczby.");
            alertError.show();
            return;
        }
        if (w < 1 || w > 500 || h < 1 || h > 500) {
            alertError.setContentText("Liczba musi być z zakresu 1 - 500");
            alertError.show();
            return;
        }
        canvasController.setW(w);
        canvasController.setH(h);

        canvasController.resetFlagRegular();
        canvasController.getCanvasCA().clearCanvas(w, h);
        canvasController.getCA().clearCA(w, h);
        canvasController.getMC().clearMC(w, h, 1.0);
        canvasController.getRMC().clearMC(w,h,1.0);
        canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
        if (buttonGenerateSeeds.isDisable()) buttonGenerateSeeds.setDisable(false);
        buttonMC.setDisable(true);
        buttonMicroEnergy.setDisable(true);
        buttonRecrystallization.setDisable(true);
        textAmountOfSeeds.setText("Liczba ziaren: " + (canvasController.getCA().getCountOfPoints() - 1));
        textIterationMC.setText("Iteracja MC: " + 0);

    }

    //Method generates seeds by random number generator or regular arrangement
    @FXML
    protected void eventGenerateSeeds() {
        String[] chTexts = generateNamesOfBoxes.getListGenerateSeedsMethod().toArray(new String[0]);
        int count = 0, numPoints;

        try {
            numPoints = Integer.parseInt(tfAmountOfSeeds.getText());
        } catch (NumberFormatException e) {
            alertError.setContentText("Nie podano liczby.");
            alertError.show();
            return;
        }

        for (int i = 0; i < canvasController.getCA().getM().length; i++) {
            for (int j = 0; j < canvasController.getCA().getM()[i].length; j++) {
                if (canvasController.getCA().getM()[i][j] == 0) count++;
            }
        }
        if (numPoints < 1 || numPoints > count) {
            alertError.setContentText("Liczba musi być większa od 0 lub za mało pustych miejsc.");
            alertError.show();
        }
        else if (choiceSeedGenerator.getValue().equals(chTexts[2])
                && (numPoints == 1 ||
                    numPoints > canvasController.getCA().getM().length ||
                    numPoints > canvasController.getCA().getM()[0].length)) {
            alertError.setContentText("Liczba musi być większa od 1 lub za mało pustych miejsc.");
            alertError.show();
        }
        else {
            if (choiceSeedGenerator.getValue().equals(chTexts[0])) {
                canvasController.setFlagRegularToTrue();
                canvasController.getCA().genRandomCAElements(numPoints, canvasController.getCanvasCA());
            }
            else {
                buttonGenerateSeeds.setDisable(true);
                canvasController.getCA().genHomogeneousCAElements(numPoints, canvasController.getW(), canvasController.getH(), canvasController.getCanvasCA());
            }
            canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
            //buttonGenPoints.setDisable(true);
            textAmountOfSeeds.setText("Liczba ziaren: " + (canvasController.getCA().getCountOfPoints() - 1));
        }
    }


    //Method generates seeds by clicking on the canvas
    @FXML
    protected void eventAreaGenerateSeeds(MouseEvent e) {
        String chText = generateNamesOfBoxes.getListGenerateSeedsMethod().get(1);
        if (choiceSeedGenerator.getValue().equals(chText)) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            boolean isAdded = canvasController.getCA().genClickedCAElements(x,y, canvasController.getW(), canvasController.getH(), canvasController.getCanvasCA());
            if (isAdded) textAmountOfSeeds.setText("Liczba ziaren: " + (canvasController.getCA().getCountOfPoints() - 1));
            canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
            canvasController.setFlagRegularToTrue();
        }
    }


    @FXML
    protected void eventHidingButtons() {
        String[] chTexts = generateNamesOfBoxes.getListGenerateSeedsMethod().toArray(new String[0]);

            if (choiceSeedGenerator.getValue().equals(chTexts[0])) {
                if (buttonGenerateSeeds.isDisable()) buttonGenerateSeeds.setDisable(false);
                tfAmountOfSeeds.setVisible(true);
                buttonGenerateSeeds.setVisible(true);
            }
            if (choiceSeedGenerator.getValue().equals(chTexts[1])) {
                tfAmountOfSeeds.setVisible(false);
                buttonGenerateSeeds.setVisible(false);

            }
            if (choiceSeedGenerator.getValue().equals(chTexts[2])) {
                if (canvasController.getFlagRegular()) buttonGenerateSeeds.setDisable(true);
                tfAmountOfSeeds.setVisible(true);
                buttonGenerateSeeds.setVisible(true);
            }
    }


    @FXML
    protected void eventRun() {
        String[] chTexts1 = generateNamesOfBoxes.getListNeighbourhood().toArray(new String[0]);
        String[] chTexts2 = generateNamesOfBoxes.getListBoundaryConditions().toArray(new String[0]);

        if (canvasController.getCA().getCountOfPoints()-1 == 0) {
            alertError.setContentText("Nie wygenerowano zarodków.");
            alertError.show();
        }
        else {
            buttonStart.setVisible(false);
            buttonStop.setVisible(true);
            AnimationTimer animationTimer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    if (choiceTypeOfModelling.getValue().equals(chTexts1[0]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                        canvasController.getCA().runPeriodicCondCA(0);
                    else if (choiceTypeOfModelling.getValue().equals(chTexts1[0]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                        canvasController.getCA().runAbsorbingCondCA(0);
                    else if (choiceTypeOfModelling.getValue().equals(chTexts1[1]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                        canvasController.getCA().runPeriodicCondCA(1);
                    else if (choiceTypeOfModelling.getValue().equals(chTexts1[1]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                        canvasController.getCA().runAbsorbingCondCA(1);
                    else if (choiceTypeOfModelling.getValue().equals(chTexts1[2]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                        canvasController.getCA().runPeriodicCondCA(2);
                    else if (choiceTypeOfModelling.getValue().equals(chTexts1[2]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                        canvasController.getCA().runAbsorbingCondCA(2);
                    else if (choiceTypeOfModelling.getValue().equals(chTexts1[3]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                        canvasController.getCA().runPeriodicCondCA(3);
                    else if (choiceTypeOfModelling.getValue().equals(chTexts1[3]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                        canvasController.getCA().runAbsorbingCondCA(3);
                    canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
                    if (canvasController.getCA().getIsEmpty()) {
                        this.stop();
                        buttonStop.setVisible(false);
                        buttonStart.setVisible(true);
                        buttonMC.setDisable(false);
                        buttonMicroEnergy.setDisable(false);
                        buttonRecrystallization.setDisable(false);
                    }
                    if (buttonGenerateArea.isPressed()) {
                        this.stop();
                        buttonStop.setVisible(false);
                        buttonStart.setVisible(true);
                    }
                    if (buttonStop.isPressed()) {
                        this.stop();
                        buttonStop.setVisible(false);
                        buttonStart.setVisible(true);
                    }
                }
            };
            animationTimer.start();
        }
    }

    @FXML
    protected void eventChangeMicrostructureEnergy() {
        if(!canvasController.isEnergyVisualization()) {
            canvasController.getCanvasCA().drawEnergy(canvasController.getMC().getEnergy());
            canvasController.setEnergyVisualization(true);
        }
        else {
            canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
            canvasController.setEnergyVisualization(false);
        }
    }

    @FXML
    protected void eventRunMC() {
        int cond = 0;
        int w = canvasController.getW();
        int h = canvasController.getH();

        String[] chTexts1 = generateNamesOfBoxes.getListNeighbourhood().toArray(new String[0]);
        String[] chTexts2 = generateNamesOfBoxes.getListBoundaryConditions().toArray(new String[0]);

        try {
            final int[] c = {0};
            cond = Integer.parseInt(tfIterationMC.getText());
            double J = Double.parseDouble(tfJ.getText());
            double kt = Double.parseDouble(tfkt.getText());
            final int[] percentage = {(w * h) / 100};
            final int[] tmp = {percentage[0]};
            final int[] point = { 0 };
            if (cond < 1 || cond > 1000) {
                alertError.setContentText("Liczba musi być z zakresu 1 - 1000");
                alertError.show();
            }
            else if (J < 0.0 || J > 10.0) {
                alertError.setContentText("Liczba musi być z zakresu 0.0 - 10.0");
                alertError.show();
            }
            else if (kt < 0.1 || kt > 6.0) {
                alertError.setContentText("Liczba musi być z zakresu 0.1 - 6");
                alertError.show();
            }
            else {
                if (canvasController.getCA().getIsEmpty()) {
                    int finalCond = cond;
                    AnimationTimer animationTimer = new AnimationTimer() {
                        @Override
                        public void handle(long l) {
                            if (choiceTypeOfModelling.getValue().equals(chTexts1[0]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(0, canvasController.getCA().getM(), true, J,kt));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[1]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(1, canvasController.getCA().getM(), true, J,kt));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[2]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(2, canvasController.getCA().getM(), true, J,kt));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[3]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(3, canvasController.getCA().getM(), true, J,kt));
                            if (choiceTypeOfModelling.getValue().equals(chTexts1[0]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(0, canvasController.getCA().getM(), false, J,kt));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[1]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(1, canvasController.getCA().getM(), false, J,kt));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[2]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(2, canvasController.getCA().getM(), false, J,kt));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[3]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getMC().runCA(3, canvasController.getCA().getM(), false, J,kt));
                            if (canvasController.getMC().getSetCount() == w * h) {
                                if (c[0] == finalCond-1) {
                                    this.stop();
                                    canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
                                    //System.out.println("STOP");
                                    canvasController.getMC().clearSet();
                                    textIterationMC.setText("Iteracja MC: " + ++c[0]);
                                    c[0] = 0;
                                }
                                else {
                                    canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
                                    //System.out.println("DONE");
                                    point[0] = 0;
                                    percentage[0] = tmp[0];
                                    textIterationMC.setText("Iteracja MC: " + c[0]);
                                    canvasController.getMC().clearSet();
                                    c[0]++;
                                }
                            }
                            if (buttonGenerateArea.isPressed()) {
                                this.stop();
                                //System.out.println("STOP");
                                canvasController.getMC().clearSet();
                                c[0] = 0;
                            }
                            if (percentage[0] == canvasController.getMC().getSetCount()) {
                                textIterationMC.setText("Iteracja MC: " + c[0]);
                                percentage[0] += tmp[0];
                            }
                        }
                    };
                    animationTimer.start();
                }
            }
        }
        catch (NumberFormatException e) {
            alertError.setContentText("Nie podano liczby.");
            alertError.show();
        }
    }


    @FXML
    protected void eventRunRecrystallization() {
        int cond = 0;
        int w = canvasController.getW();
        int h = canvasController.getH();

        String[] chTexts1 = generateNamesOfBoxes.getListNeighbourhood().toArray(new String[0]);
        String[] chTexts2 = generateNamesOfBoxes.getListBoundaryConditions().toArray(new String[0]);

        try {
            final int[] c = {0};
            cond = Integer.parseInt(tfIterationMC.getText());
            double J = Double.parseDouble(tfJ.getText());
            int el = Integer.parseInt(tfAmountOfNewSeeds.getText());
            double XX = Double.parseDouble(tfXX.getText());
            double YY = Double.parseDouble(tfYY.getText());
            final int[] percentage = {(w * h) / 100};
            final int[] tmp = {percentage[0]};
            final int[] point = { 0 };
            if (cond < 1 || cond > 1000) {
                alertError.setContentText("Liczba musi być z zakresu 1 - 1000");
                alertError.show();
            }
            else if (J < 0.0 || J > 10.0) {
                alertError.setContentText("Liczba musi być z zakresu 0.0 - 10.0");
                alertError.show();
            }
            else if (el < 1 || el > 1000) {
                alertError.setContentText("Liczba musi być większa od 0 lub za mało dostępnych miejsc.");
                alertError.show();
            }
            else if (XX < 0 || XX > 5.0 || YY < 0 || YY > 5.0) {
                alertError.setContentText("Liczba musi być z zakresu 0.0 - 5.0");
                alertError.show();
            }
            else {
                if (canvasController.getCA().getIsEmpty()) {
                    int finalCond = cond;
                    canvasController.getCA().addNewGrains(el, canvasController.getCanvasCA());
                    //canvasController.getRMC().addNumOfGrains(canvasController.getCA().getCountOfPoints());
                    canvasController.getRMC().addNumOfGrains(canvasController.getCA().getCountOfPoints()-el);
                    AnimationTimer animationTimer = new AnimationTimer() {
                        @Override
                        public void handle(long l) {
                            if (choiceTypeOfModelling.getValue().equals(chTexts1[0]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(0, canvasController.getCA().getM(), true, J, XX, YY));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[1]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(1, canvasController.getCA().getM(), true, J, XX, YY));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[2]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(2, canvasController.getCA().getM(), true, J, XX, YY));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[3]) && choiceBoundaryConditions.getValue().equals(chTexts2[0]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(3, canvasController.getCA().getM(), true, J, XX, YY));
                            if (choiceTypeOfModelling.getValue().equals(chTexts1[0]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(0, canvasController.getCA().getM(), false, J, XX, YY));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[1]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(1, canvasController.getCA().getM(), false, J, XX, YY));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[2]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(2, canvasController.getCA().getM(), false, J, XX, YY));
                            else if (choiceTypeOfModelling.getValue().equals(chTexts1[3]) && choiceBoundaryConditions.getValue().equals(chTexts2[1]))
                                canvasController.getCA().setM(canvasController.getRMC().runCA(3, canvasController.getCA().getM(), false, J, XX, YY));
                            if (canvasController.getRMC().getSetCount() == w * h) {
                                if (c[0] == finalCond-1) {
                                    this.stop();
                                    canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
                                    canvasController.getRMC().clearSet();
                                    textIterationMC.setText("Iteracja MC: " + ++c[0]);
                                    c[0] = 0;
                                }
                                else {
                                    canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
                                    point[0] = 0;
                                    percentage[0] = tmp[0];
                                    textIterationMC.setText("Iteracja MC: " + c[0]);
                                    canvasController.getRMC().clearSet();
                                    c[0]++;
                                }
                            }
                            if (buttonGenerateArea.isPressed()) {
                                this.stop();
                                //System.out.println("STOP");
                                canvasController.getRMC().clearSet();
                                c[0] = 0;
                            }
                            if (percentage[0] == canvasController.getRMC().getSetCount()) {
                                textIterationMC.setText("Iteracja MC: " + c[0]);
                                percentage[0] += tmp[0];
                            }
                        }
                    };
                    animationTimer.start();
                }
            }
        }
        catch (NumberFormatException e) {
            alertError.setContentText("Nie podano liczby.");
            alertError.show();
        }
    }

    @FXML
    protected void eventSaveFile() {
        FileChooser savefile = new FileChooser();
        savefile.setTitle("Zapisz plik");

        int w = canvasController.getW();
        int h = canvasController.getH();
        stage = (Stage) gridPane.getScene().getWindow();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files", "*.PNG");
        savefile.getExtensionFilters().add(extFilter);

        File file = savefile.showSaveDialog(stage);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage(w+2, h+2);
                canvasController.getCanvasCA().getCanvas().snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("Error!");
            }
        }
    }

    @FXML
    protected void eventLoadFile() {
        BufferedImage img = null;
        File f;

        int w;
        int h;

        try {
            f = new File("area.png");  //!!!!!!!!!!!!!!!
            img = ImageIO.read(f);
            int[][] mm = new int[img.getHeight()][img.getWidth()];
            for (int i = 0; i < img.getHeight(); i++) {
                for (int j = 0; j < img.getWidth(); j++) {
                    int p = img.getRGB(j, i);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    if (r == 0 && g == 0 && b == 0) mm[i][j] = -1;
                    else mm[i][j] = 0;
                }
            }
            w = img.getWidth();
            h = img.getHeight();
            if (w < 1 || w > 500 || h < 1 || h > 500) {
                alertError.setContentText("Liczba musi być z zakresu 1 - 500");
                alertError.show();
            } else {
                canvasController.resetFlagRegular();
                canvasController.getCanvasCA().clearCanvas(w, h);
                canvasController.getCA().clearCA(w, h);
                canvasController.getMC().clearMC(w, h, 1.0);
                canvasController.getRMC().clearMC(w, h, 1.0);
                canvasController.getCA().setM(mm);
                canvasController.getCanvasCA().drawImage(canvasController.getCA().getM());
                if (buttonGenerateSeeds.isDisable()) buttonGenerateSeeds.setDisable(false);
                buttonMC.setDisable(true);
                buttonMicroEnergy.setDisable(true);
                buttonRecrystallization.setDisable(true);
                textAmountOfSeeds.setText("Liczba ziaren: " + (canvasController.getCA().getCountOfPoints() - 1));
                textIterationMC.setText("Iteracja MC: " + 0);
            }

            canvasController.setW(w);
            canvasController.setH(h);


        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        generateNamesOfBoxes = new GenerateNamesOfBoxes(filename);
        alertError = new Alert(Alert.AlertType.ERROR);
        canvasController = new CanvasController(500,500, canvas);

        generateNamesOfBoxes.setDatafromFile();
        generateNamesOfBoxes.setAllValuesGenSeeds(choiceSeedGenerator,observableList);
        generateNamesOfBoxes.setAllValuesNeighbourhood(choiceTypeOfModelling,observableList);
        generateNamesOfBoxes.setAllValuesBoundaryConditions(choiceBoundaryConditions,observableList);

        textAmountOfSeeds.setText("Liczba ziaren: "+ 0);
        textIterationMC.setText("Liczba iteracji: " + 0);
    }
}