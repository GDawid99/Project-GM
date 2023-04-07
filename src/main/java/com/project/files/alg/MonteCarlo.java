package com.project.files.alg;


import com.project.files.alg.neighbourhood.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MonteCarlo {
    private double[][] energy;
    private int w, h;
    private double E;
    private double J;
    private double kt;
    private Set<Integer[]> set;
    private Neighbourhood neighbourhoodType;

    public MonteCarlo(int w, int h, double J) {
        this.w = w;
        this.h = h;
        this.J = J;
        set = new HashSet<>();
        energy = new double[w][h];
    }

    public void clearMC(int w, int h, double J) {
        this.w = w;
        this.h = h;
        E = 0;
        this.J = J;
        set.clear();
        for(int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                energy[i][j] = 0;
            }
        }
    }

    public double[][] getEnergy() {
        return energy;
    }

    //neighbourhood: 0 - von Neumann, 1 - Moore
    // 2 - Pentagonal, 3 - Hexagonal
    private com.project.files.alg.Data calculateEnergy(int i, int j, int el1, int el2, int el3, int el4, int neighbourhood, int[][] m, boolean isRandomElement) {
        if (neighbourhood == 0) neighbourhoodType = new VonNeumann();
        else if (neighbourhood == 1) neighbourhoodType = new Moore();
        else if (neighbourhood == 2) neighbourhoodType = new Pentagonal();
        else neighbourhoodType = new Hexagonal();

        int[] s = neighbourhoodType.mcFunIf(i, j, el1, el2, el3, el4, m, isRandomElement);
        if (s[0] == 9) return new Data();
        return new Data(J*s[0],s[1]);
    }

    public int[][] randomCAElement(int neighbourhood, int[][] m, boolean isPeriodic) {
        double tmpEnergy;
        double deltaE;
        Data s;
        int id;
        int i = new Random().nextInt(h);
        int j = new Random().nextInt(w);
        set.add(new Integer[] {i,j});
        if (m[i][j] == -1) return m;

        if (i == 0) {
            if (j == 0) {   //lewy gorny rog
                if (isPeriodic) return calculate(i, j, h - 1, i + 1, w - 1, j + 1, neighbourhood, m);
                else return calculate(i, j, i, i + 1, j, j + 1, neighbourhood, m);
            } else if (j == w - 1) {  //prawy gorny rog
                if (isPeriodic) return calculate(i, j, h - 1, i + 1, j - 1, 0, neighbourhood, m);
                else return calculate(i, j, i, i + 1, j-1, w-1, neighbourhood, m);
            } else { //pomiedzy
                if (isPeriodic) return calculate(i, j, h - 1, i + 1, j - 1, j + 1, neighbourhood, m);
                else return calculate(i, j, i, i + 1, j - 1, j + 1, neighbourhood, m);
            }
        } else if (i == h - 1) {
            if (j == 0) {   //lewy dolny rog
                if (isPeriodic) return calculate(i, j, i - 1, 0, w - 1, j + 1, neighbourhood, m);
                else return calculate(i, j, i - 1, h - 1, j, j + 1, neighbourhood, m);
            } else if (j == w - 1) {  //prawy dolny rog
                if (isPeriodic) return calculate(i, j, i - 1, 0, j - 1, 0, neighbourhood, m);
                else return calculate(i, j, i - 1, h - 1, j - 1, w - 1, neighbourhood, m);
            } else { //pomiedzy
                if (isPeriodic) return calculate(i, j, i - 1, 0, j - 1, j + 1, neighbourhood, m);
                else return calculate(i, j, i - 1, h - 1, j - 1, j + 1, neighbourhood, m);
            }
        } else if (j == 0) {
            if (isPeriodic) return calculate(i, j, i - 1, i + 1, w - 1, j + 1, neighbourhood, m);
            else return calculate(i, j, i - 1, i + 1, j, j + 1, neighbourhood, m);
        } else if (j == w - 1) {
            if (isPeriodic) return calculate(i, j, i - 1, i + 1, j - 1, 0, neighbourhood, m);
            else return calculate(i, j, i - 1, i + 1, j - 1, w - 1, neighbourhood, m);
        } else {
            return calculate(i, j, i - 1, i + 1, j - 1, j + 1, neighbourhood, m);
        }
    }


    private int[][] calculate(int i, int j, int el1, int el2, int el3, int el4, int neighbourhood, int[][] m) {
        Data s;
        double tmpEnergy;
        int id;
        double deltaE;

        s = calculateEnergy(i, j, el1, el2, el3, el4, neighbourhood, m, false);
        if (s.getId() == 9) return m;

        energy[i][j] = s.getE();
        s = calculateEnergy(i, j, el1, el2, el3, el4, neighbourhood, m, true);
        if (s.getId() == 9) return m;

        tmpEnergy = s.getE();
        id = s.getId();
        deltaE = tmpEnergy - energy[i][j];
        if (deltaE < 0 || new Random().nextDouble() < Math.exp(-deltaE/kt)) {
            energy[i][j] = tmpEnergy;
            m[i][j] = id;
        }
        return m;
    }


    public int[][] runCA(int neighbourhood, int[][] m, boolean isPeriodic, double J, double kt) {
        this.J = J;
        this.kt = kt;
        while(set.size() < w*h) {
            m = randomCAElement(neighbourhood,m,isPeriodic);
        }
        return m;
    }


    public void clearSet(){
        set.clear();
    }

    public Set<Integer[]> getSet() {
        return set;
    }

    public int getSetCount() {
        return set.size();
    }


}
