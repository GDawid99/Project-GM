package com.project.files;

import com.project.files.alg.CellularAutomaton;
import com.project.files.alg.MonteCarlo;
import com.project.files.alg.RecrystallizationMC;
import com.project.files.gui.CanvasCA;
import javafx.scene.canvas.Canvas;

public class CanvasController {
    private CanvasCA canvasCA;
    private CellularAutomaton ca;
    private MonteCarlo mc;
    private RecrystallizationMC rmc;
    private int w;
    private int h;
    private double J;
    private double kt;
    private boolean flagRegular;
    private boolean isEnergyVisualization;

    public CanvasController(int w, int h, Canvas canvas) {
        this.w = w;
        this.h = h;
        J = 1.0;
        kt = 0.5;
        ca = new CellularAutomaton(w, h);
        mc = new MonteCarlo(w,h,J);
        rmc = new RecrystallizationMC(w,h,J);
        canvasCA = new CanvasCA(w,h,canvas);
        canvasCA.drawImage(ca.getM());
        flagRegular = false;
        isEnergyVisualization = false;
    }

    public boolean isEnergyVisualization() {
        return isEnergyVisualization;
    }

    public void setEnergyVisualization(boolean energyVisualization) {
        isEnergyVisualization = energyVisualization;
    }

    public void setFlagRegularToTrue() {
        flagRegular = true;
    }

    public void resetFlagRegular() {
        flagRegular = false;
    }

    public boolean getFlagRegular() {
        return flagRegular;
    }

    public CanvasCA getCanvasCA() {
        return canvasCA;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public CellularAutomaton getCA() {
        return ca;
    }

    public RecrystallizationMC getRMC() {
        return rmc;
    }

    public MonteCarlo getMC() {
        return mc;
    }
}
