package com.project.files.alg;

import com.project.files.alg.neighbourhood.*;
import com.project.files.gui.CanvasCA;

import java.util.Random;

public class CellularAutomaton {
    private Random r;
    private int[][] m;
    private int w;
    private int h;
    private boolean isEmpty;
    private int countOfPoints;
    private Neighbourhood neighbourhoodType;

    public CellularAutomaton(int w, int h) {
        this.w = w;
        this.h = h;
        countOfPoints = 1;
        isEmpty = false;
        r = new Random();
        m = new int[h][w];
    }

    public int[][] getM() {
        return m;
    }

    public void setM(int[][] m) {
        this.m = m;
    }

    public void clearCA(int w, int h) {
        this.w = w;
        this.h = h;
        countOfPoints = 1;
        isEmpty = false;
        m = new int[h][w];
        for(int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                m[i][j] = 0;
            }
        }
    }

    public boolean getIsEmpty() { return isEmpty; }

    public void genRandomCAElements(int numOfEl, CanvasCA c) {
        int el1, el2;
        for (int i = 1; i < numOfEl+1; i++) {
            do{
                el1 = r.nextInt(h);
                el2 = r.nextInt(w);
            } while (m[el1][el2] != 0);
            m[el1][el2] = countOfPoints;
            c.addRandomColor(countOfPoints);
            countOfPoints++;
        }
    }

    public void genHomogeneousCAElements(int numOfEl,int w, int h, CanvasCA c) {
        int nx, ny;
        nx = w;
        ny = h;
        int rx, ry;
        rx = ry = numOfEl;

        int quotientx = (nx-1) / (rx-1);
        int remainderx = (nx-1) % (rx-1);

        int quotienty = (ny-1) / (ry-1);
        int remaindery = (ny-1) % (ry-1);

        int index_x = 0;
        int index_y = 0;
        do {
            do {
                m[index_y][index_x] = countOfPoints;
                c.addRandomColor(countOfPoints);
                countOfPoints++;
            } while( (index_x += quotientx + (remainderx-- > 0 ? 1 : 0)) < nx );
            index_x = 0;
            quotientx = (nx-1) / (rx-1);
            remainderx = (nx-1) % (rx-1);
        } while( (index_y += quotienty + (remaindery-- > 0 ? 1 : 0)) < ny );

    }

    public boolean genClickedCAElements(int x, int y, int w, int h, CanvasCA c) {
        if (x == 0 || y == 0 || x == w+1 || y == h+1) {
            return false;
        }
        if (m[y-1][x-1] != 0) {
            return false;
        }
        m[y-1][x-1] = countOfPoints;
        c.addRandomColor(countOfPoints);
        countOfPoints++;
        return true;
    }

    public void addNewGrains(int numOfNewGrains,CanvasCA c) {
        int el1, el2;
        int oldValue = countOfPoints;
        for (int i = 0; i < numOfNewGrains; i++) {
            do{
                el1 = r.nextInt(h-2)+1;
                el2 = r.nextInt(w-2)+1;
            } while (m[el1][el2] > oldValue || m[el1][el2] == -1);
            m[el1][el2] = countOfPoints;
            c.addRandomColor(countOfPoints);
            countOfPoints++;
        }
    }

    public void runPeriodicCondCA(int neighbourhood){
        int[][] tmpM = new int[h][w];
        int count = 0;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (m[i][j] == 0) {
                    count++;
                    if (i == 0) {
                        if (j == 0) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, h - 1, i + 1, w - 1, j + 1); //lewy gorny rog
                        else if (j == w - 1) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, h - 1, i + 1, j - 1, 0); //prawy gorny rog
                        else tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, h - 1, i + 1, j - 1, j + 1);  //pomiedzy
                    }
                    else if (i == h - 1) {
                        if (j == 0) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, 0, w - 1, j + 1); //lewy dolny rog
                        else if (j == w - 1) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, 0, j - 1, 0); //prawy dolny rog
                        else tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, 0, j - 1, j + 1); //pomiedzy
                    }
                    else if (j == 0) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, i + 1, w - 1, j + 1);
                    else if (j == w - 1) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, i + 1, j - 1, 0);
                    else tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, i + 1, j - 1, j + 1);
                } else tmpM[i][j] = m[i][j];
            }
        }
        m = tmpM;
        if (count == 0) isEmpty = true;
    }

    public void runAbsorbingCondCA(int neighbourhood){
        int[][] tmpM = new int[h][w];
        int count = 0;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (m[i][j] == 0) {
                    count++;
                    if (i == 0) {
                        if (j == 0) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i, i + 1, j, j + 1); //lewy gorny rog
                        else if (j == w - 1) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i, i + 1, j - 1, w - 1); //prawy gorny rog
                        else tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i, i + 1, j - 1, j + 1); //pomiedzy
                    }
                    else if (i == h - 1) {
                        if (j == 0) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, h - 1, j, j + 1); //lewy dolny rog
                        else if (j == w - 1) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, h - 1, j - 1, w - 1); //prawy dolny rog
                        else tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, h - 1, j - 1, j + 1); //pomiedzy
                    }
                    else if (j == 0) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, i + 1, j, j + 1);
                    else if (j == w - 1) tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, i + 1, j - 1, w - 1);
                    else tmpM[i][j] = calculate(neighbourhood, tmpM, i, j, i - 1, i + 1, j - 1, j + 1);
                } else tmpM[i][j] = m[i][j];
            }
        }
        m = tmpM;
        if (count == 0) isEmpty = true;
    }

    private int calculate(int neighbourhood, int[][] tmpM, int ... els) {
        if (neighbourhood == 0) neighbourhoodType = new VonNeumann();
        else if (neighbourhood == 1) neighbourhoodType = new Moore();
        else if (neighbourhood == 2) neighbourhoodType = new Pentagonal();
        else if (neighbourhood == 3) neighbourhoodType = new Hexagonal();
        return tmpM[els[0]][els[1]] = neighbourhoodType.funIf(els[0], els[1], els[2], els[3], els[4], els[5], m, tmpM);
    }

    public int getCountOfPoints() {
        return countOfPoints;
    }

}
