package com.project.files.alg;

import com.project.files.alg.neighbourhood.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RecrystallizationMC {
    private double[][] energy;
    private int w, h;
    private double J;
    private int numOfGrains;
    private Set<Integer[]> set;
    private Neighbourhood neighbourhoodType;

    public RecrystallizationMC(int w, int h, double J) {
        this.w = w;
        this.h = h;
        this.J = J;
        this.numOfGrains = 2;
        set = new HashSet<>();
        energy = new double[w][h];
    }

    public void clearMC(int w, int h, double J) {
        this.w = w;
        this.h = h;
        this.J = J;
        this.numOfGrains = 2;
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

    public void addNumOfGrains(int numOfGrains) {
        this.numOfGrains += numOfGrains;
    }

    //neighbourhood: 0 - von Neumann, 1 - Moore
    // 2 - Pentagonal, 3 - Hexagonal
    private Data calculateEnergy(int i, int j, int el1, int el2, int el3, int el4, int neighbourhood, int[][] m, boolean isRandomElement, double XX, double YY) {
        double H;
        if (neighbourhood == 0) neighbourhoodType = new VonNeumann();
        else if (neighbourhood == 1) neighbourhoodType = new Moore();
        else if (neighbourhood == 2) neighbourhoodType = new Pentagonal();
        else neighbourhoodType = new Hexagonal();

        int[] s = neighbourhoodType.mcFunIf(i, j, el1, el2, el3, el4, m, isRandomElement);
        if (s[1] < numOfGrains && isRandomElement) return new Data();   //wylosowany sąsiad nie jest nowym zarodkiem
        if (s[0] == 0 && isRandomElement) H = -XX;
        else if (s[0] > 0 && isRandomElement) H = -YY;
        else H = 0.0;
        return new Data(J*s[0]+H,s[1]);
    }



    public int[][] recrystallization(int neighbourhood, int[][] m, boolean isPeriodic, double XX, double YY) {
        double tmpEnergy;
        double deltaE;
        Data s;
        int id;
        int i = new Random().nextInt(h);
        int j = new Random().nextInt(w);
        double chance = 0.1;

        set.add(new Integer[] {i,j});

        if (i == 0) {
            if (j == 0) {   //lewy gorny rog
                if (isPeriodic) s = calculateEnergy(i, j, h - 1, i + 1, w - 1, j + 1, neighbourhood, m, false, XX, YY);
                else s = calculateEnergy(i, j, i, i + 1, j, j + 1, neighbourhood, m, false, XX, YY);
                energy[i][j] = s.getE();
                if (isPeriodic) s = calculateEnergy(i, j, h - 1, i + 1, w - 1, j + 1, neighbourhood, m, true, XX, YY);
                else s = calculateEnergy(i, j, i, i + 1, j, j + 1, neighbourhood, m, true, XX, YY);
                if (s.getId() == 0) return m;
                tmpEnergy = s.getE();  id = s.getId();
                deltaE = tmpEnergy - energy[i][j];
                if (deltaE < 0 || new Random().nextDouble() < chance) {
                    energy[i][j] = tmpEnergy;
                    m[i][j] = id;
                }
            } else if (j == w - 1) {  //prawy gorny rog
                if (isPeriodic) s = calculateEnergy(i, j, h - 1, i + 1, j - 1, 0, neighbourhood, m, false, XX, YY);
                else s = calculateEnergy(i, j, i, i + 1, j-1, w-1, neighbourhood, m, false, XX, YY);
                energy[i][j] = s.getE();
                if (isPeriodic) s = calculateEnergy(i, j, h - 1, i + 1, j - 1, 0, neighbourhood, m, true, XX, YY);
                else s = calculateEnergy(i, j, i, i + 1, j-1, w-1, neighbourhood, m, true, XX, YY);
                if (s.getId() == 0) return m;
                tmpEnergy = s.getE();
                id = s.getId();
                deltaE = tmpEnergy - energy[i][j];
                if (deltaE < 0 || new Random().nextDouble() < chance) {
                    energy[i][j] = tmpEnergy;
                    m[i][j] = id;
                }
            } else { //pomiedzy
                if (isPeriodic) s = calculateEnergy(i, j, h - 1, i + 1, j - 1, j + 1, neighbourhood, m, false, XX, YY);
                else s = calculateEnergy(i, j, i, i + 1, j - 1, j + 1, neighbourhood, m, false, XX, YY);
                energy[i][j] = s.getE();
                if (isPeriodic) s = calculateEnergy(i, j, h - 1, i + 1, j - 1, j + 1, neighbourhood, m, true, XX, YY);
                else s = calculateEnergy(i, j, i, i + 1, j - 1, j + 1, neighbourhood, m, true, XX, YY);
                if (s.getId() == 0) return m;
                tmpEnergy = s.getE();
                id = s.getId();
                deltaE = tmpEnergy - energy[i][j];
                if (deltaE < 0 || new Random().nextDouble() < chance) {
                    energy[i][j] = tmpEnergy;
                    m[i][j] = id;
                }
            }
        } else if (i == h - 1) {
            if (j == 0) {   //lewy dolny rog
                if (isPeriodic) s = calculateEnergy(i, j, i - 1, 0, w - 1, j + 1, neighbourhood, m, false, XX, YY);
                else s = calculateEnergy(i, j, i - 1, h - 1, j, j + 1, neighbourhood, m, false, XX, YY);
                energy[i][j] = s.getE();
                if (isPeriodic) s = calculateEnergy(i, j, i - 1, 0, w - 1, j + 1, neighbourhood, m, true, XX, YY);
                else s = calculateEnergy(i, j, i - 1, h - 1, j, j + 1, neighbourhood, m, true, XX, YY);
                if (s.getId() == 0) return m;
                tmpEnergy = s.getE();
                id = s.getId();
                deltaE = tmpEnergy - energy[i][j];
                if (deltaE < 0 || new Random().nextDouble() < chance) {
                    energy[i][j] = tmpEnergy;
                    m[i][j] = id;
                }
            } else if (j == w - 1) {  //prawy dolny rog
                if (isPeriodic) s = calculateEnergy(i, j, i - 1, 0, j - 1, 0, neighbourhood, m, false, XX, YY);
                else s = calculateEnergy(i, j, i - 1, h - 1, j - 1, w - 1, neighbourhood, m, false, XX, YY);
                energy[i][j] = s.getE();
                if (isPeriodic) s = calculateEnergy(i, j, i - 1, 0, j - 1, 0, neighbourhood, m, true, XX, YY);
                else s = calculateEnergy(i, j, i - 1, h - 1, j - 1, w - 1, neighbourhood, m, true, XX, YY);
                if (s.getId() == 0) return m;
                tmpEnergy = s.getE();
                id = s.getId();
                deltaE = tmpEnergy - energy[i][j];
                if (deltaE < 0 || new Random().nextDouble() < chance) {
                    energy[i][j] = tmpEnergy;
                    m[i][j] = id;
                }
            } else { //pomiedzy
                if (isPeriodic) s = calculateEnergy(i, j, i - 1, 0, j - 1, j + 1, neighbourhood, m, false, XX, YY);
                else s = calculateEnergy(i, j, i - 1, h - 1, j - 1, j + 1, neighbourhood, m, false, XX, YY);
                energy[i][j] = s.getE();
                if (isPeriodic) s = calculateEnergy(i, j, i - 1, 0, j - 1, j + 1, neighbourhood, m, true, XX, YY);
                else s = calculateEnergy(i, j, i - 1, h - 1, j - 1, j + 1, neighbourhood, m, true, XX, YY);
                if (s.getId() == 0) return m;
                tmpEnergy = s.getE();
                id = s.getId();
                deltaE = tmpEnergy - energy[i][j];
                if (deltaE < 0 || new Random().nextDouble() < chance) {
                    energy[i][j] = tmpEnergy;
                    m[i][j] = id;
                }
            }
        } else if (j == 0) {
            if (isPeriodic) s = calculateEnergy(i, j, i - 1, i + 1, w - 1, j + 1, neighbourhood, m, false, XX, YY);
            else s = calculateEnergy(i, j, i - 1, i + 1, j, j + 1, neighbourhood, m, false, XX, YY);
            energy[i][j] = s.getE();
            if (isPeriodic) s = calculateEnergy(i, j, i - 1, i + 1, w - 1, j + 1, neighbourhood, m, true, XX, YY);
            else s = calculateEnergy(i, j, i - 1, i + 1, j, j + 1, neighbourhood, m, true, XX, YY);
            if (s.getId() == 0) return m;
            tmpEnergy = s.getE();
            id = s.getId();
            deltaE = tmpEnergy - energy[i][j];
            if (deltaE < 0 || new Random().nextDouble() < chance) {
                energy[i][j] = tmpEnergy;
                m[i][j] = id;
            }
        } else if (j == w - 1) {
            if (isPeriodic) s = calculateEnergy(i, j, i - 1, i + 1, j - 1, 0, neighbourhood, m, false, XX, YY);
            else s = calculateEnergy(i, j, i - 1, i + 1, j - 1, w - 1, neighbourhood, m, false, XX, YY);
            energy[i][j] = s.getE();
            if (isPeriodic) s = calculateEnergy(i, j, i - 1, i + 1, j - 1, 0, neighbourhood, m, true, XX, YY);
            else s = calculateEnergy(i, j, i - 1, i + 1, j - 1, w - 1, neighbourhood, m, true, XX, YY);
            if (s.getId() == 0) return m;
            tmpEnergy = s.getE();
            id = s.getId();
            deltaE = tmpEnergy - energy[i][j];
            if (deltaE < 0 || new Random().nextDouble() < chance) {
                energy[i][j] = tmpEnergy;
                m[i][j] = id;
            }
        } else {
            if (isPeriodic) s = calculateEnergy(i, j, i - 1, i + 1, j - 1, j + 1, neighbourhood, m, false, XX, YY);
            else s = calculateEnergy(i, j, i - 1, i + 1, j - 1, j + 1, neighbourhood, m, false, XX, YY);
            energy[i][j] = s.getE();
            if (isPeriodic) s = calculateEnergy(i, j, i - 1, i + 1, j - 1, j + 1, neighbourhood, m, true, XX, YY);
            else s = calculateEnergy(i, j, i - 1, i + 1, j - 1, j + 1, neighbourhood, m, true, XX, YY);
            if (s.getId() == 0) return m;
            tmpEnergy = s.getE();
            id = s.getId();
            deltaE = tmpEnergy - energy[i][j];
            if (deltaE < 0 || new Random().nextDouble() < chance) {
                energy[i][j] = tmpEnergy;
                m[i][j] = id;
            }
        }
        return m;
    }

    public int[][] runCA(int neighbourhood, int[][] m, boolean isPeriodic, double J, double XX, double YY) {
        this.J = J;
        while(set.size() < w*h) {
            m = recrystallization(neighbourhood,m,isPeriodic, XX, YY);
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
