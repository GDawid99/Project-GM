package com.project.files.alg.neighbourhood;

import java.util.*;

public class Pentagonal extends Neighbourhood{

    @Override
    public int funIf(int i, int j, int el1, int el2, int el3, int el4, int[][] m, int[][] tmpM) {
        map = new HashMap<>();
        int count = 0;

        //random: 0 - gora, 1 - dol, 2 - lewo, 3 - prawo
        int r = new Random().nextInt(4);

        if(m[el1][j] > 0 && (r == 0 || r == 2 || r == 3)) map.put(m[el1][j],0); //gora
        if(m[el2][j] > 0 && (r == 1 || r == 2 || r == 3)) map.put(m[el2][j],0); //dol
        if(m[i][el3] > 0 && (r == 0 || r == 1 || r == 2)) map.put(m[i][el3],0); //lewo
        if(m[i][el4] > 0 && (r == 0 || r == 1 || r == 3)) map.put(m[i][el4],0); //prawo
        if(m[el1][el3] > 0 && (r == 0 || r == 2)) map.put(m[el1][el3],0); //lewy gorny
        if(m[el1][el4] > 0 && (r == 0 || r == 3)) map.put(m[el1][el4],0); //prawy gorny
        if(m[el2][el3] > 0 && (r == 1 || r == 2)) map.put(m[el2][el3],0); //lewy dolny
        if(m[el2][el4] > 0 && (r == 1 || r == 3)) map.put(m[el2][el4],0); //prawy dolny

        if (m[i][j] == 0) {
            if (m[el1][j] != 0 && map.get(m[el1][j]) != null) {
                map.put(m[el1][j],map.get(m[el1][j])+1);
                count++;
            }
            if (m[el2][j] != 0 && map.get(m[el2][j]) != null) {
                map.put(m[el2][j],map.get(m[el2][j])+1);
                count++;
            }
            if (m[i][el3] != 0 && map.get(m[i][el3]) != null) {
                map.put(m[i][el3],map.get(m[i][el3])+1);
                count++;
            }
            if (m[i][el4] != 0 && map.get(m[i][el4]) != null) {
                map.put(m[i][el4],map.get(m[i][el4])+1);
                count++;
            }
            if (m[el1][el3] != 0 && map.get(m[el1][el3]) != null) {   //lewy gorny
                map.put(m[el1][el3],map.get(m[el1][el3])+1);
                count++;
            }
            if (m[el1][el4] != 0 && map.get(m[el1][el4]) != null) {   //prawy gorny
                map.put(m[el1][el4],map.get(m[el1][el4])+1);
                count++;
            }
            if (m[el2][el3] != 0 && map.get(m[el2][el3]) != null) {   //lewy dolny
                map.put(m[el2][el3],map.get(m[el2][el3])+1);
                count++;
            }
            if (m[el2][el4] != 0 && map.get(m[el2][el4]) != null) {   //prawy dolny
                map.put(m[el2][el4],map.get(m[el2][el4])+1);
                count++;
            }

            if (count>0) {
                int tmp = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
                tmpM[i][j] = tmp;
            }
            else tmpM[i][j] = m[i][j];
            if(map != null) map.clear();
        }
        else
            tmpM[i][j] = m[i][j];
        return tmpM[i][j];
    }

    @Override
    public int[] mcFunIf(int i, int j, int el1, int el2, int el3, int el4, int[][] m, boolean isRandomElement) {
        list = new ArrayList<>();
        int count, randomNum, id, newID;
        if (m[i][j] == -1) return new int[] {9,-1};
        randomNum = new Random().nextInt(4);
        if (randomNum != 1 && m[el1][j] != -1) list.add(m[el1][j]);
        if (randomNum != 0 && m[el2][j] != -1) list.add(m[el2][j]);
        if (randomNum != 3 && m[i][el3] != -1) list.add(m[i][el3]);
        if (randomNum != 2 && m[i][el4] != -1) list.add(m[i][el4]);
        if ((randomNum == 0 || randomNum == 2) && m[el1][el3] != -1) list.add(m[el1][el3]);
        if ((randomNum == 0 || randomNum == 3) && m[el1][el4] != -1) list.add(m[el1][el4]);
        if ((randomNum == 1 || randomNum == 2) && m[el2][el3] != -1) list.add(m[el2][el3]);
        if ((randomNum == 1 || randomNum == 3) && m[el2][el4] != -1) list.add(m[el2][el4]);
        if (list.size() == 0) return new int[] {9,-1};
        if (isRandomElement) newID = list.get(new Random().nextInt(list.size()));
        else newID = m[i][j];
        count = (int)list.stream().filter(n -> !n.equals(newID)).count();
        id = newID;
        list.clear();
        return new int[] {count, id};
    }

}
