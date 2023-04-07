package com.project.files.gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CanvasCA {

    private Canvas canvas;
    private GraphicsContext gc;
    private Map<Integer,RGBColor> colors;
    private Random r;

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public CanvasCA(int w, int h) {
        canvas = new Canvas(w+2,h+2);
        gc = canvas.getGraphicsContext2D();
        colors = new HashMap<>();
        r = new Random();
    }

    public CanvasCA(int w, int h, Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        colors = new HashMap<>();
        r = new Random();
    }



    public void addRandomColor(int idOfEl) {
        RGBColor rgbColor;
        do {
            rgbColor = new RGBColor(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        } while (isNewColor(colors,rgbColor));
        colors.put(idOfEl, new RGBColor(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
    }

    private boolean isNewColor(Map<Integer,RGBColor> m, RGBColor c) {
        for (Map.Entry<Integer,RGBColor> entry: m.entrySet()) {
            if(entry.getValue().equals(c)) {
                return true;
            }
        }
        return false;
    }

    public void addColor(int idOfEl, RGBColor rgb) {
        colors.put(idOfEl,rgb);
    }

    public void drawImage(int[][] m) {
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        for (int i = 0; i < m.length+2; i++) {
            for (int j = 0; j < m[0].length+2; j++) {
                if (i == 0 || j == 0 || i == m.length+1 || j == m[0].length+1) {
                    gc.getPixelWriter().setColor(j, i, Color.rgb(0,0,0));
                }
                else if (m[i-1][j-1] == -1){
                    gc.getPixelWriter().setColor(j, i, Color.rgb(0,0,0));
                }
                else if (m[i-1][j-1] != 0) {
                        gc.getPixelWriter().setColor(j, i, Color.rgb(
                                colors.get(m[i - 1][j - 1]).getR(),
                                colors.get(m[i - 1][j - 1]).getG(),
                                colors.get(m[i - 1][j - 1]).getB()));
                }
            }
        }
    }

    public void drawEnergy(double[][] e) {
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        for (int i = 0; i < e.length+2; i++) {
            for (int j = 0; j < e[0].length+2; j++) {
                if (i == 0 || j == 0 || i == e.length+1 || j == e[0].length+1) {
                    gc.getPixelWriter().setColor(j, i, Color.rgb(0,0,0));
                }
                else if (e[i-1][j-1] > 0) {
                    gc.getPixelWriter().setColor(j, i, Color.rgb(0,0,0));
                }
            }
        }
    }

    public void clearCanvas(int w, int h) {
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.setWidth(w+2);
        canvas.setHeight(h+2);
        colors.clear();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }
}
