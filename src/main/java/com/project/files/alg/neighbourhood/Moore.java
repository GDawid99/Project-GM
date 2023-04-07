package com.project.files.alg.neighbourhood;

import java.util.*;

public class Moore extends Neighbourhood{

    @Override
    public int funIf(int i, int j, int el1, int el2, int el3, int el4, int[][] m, int[][] tmpM) {
        map = new HashMap<>();
        int count = 0;

        if(m[el1][j] > 0) map.put(m[el1][j],0); //gora
        if(m[el2][j] > 0) map.put(m[el2][j],0); //dol
        if(m[i][el3] > 0) map.put(m[i][el3],0); //lewo
        if(m[i][el4] > 0) map.put(m[i][el4],0); //prawo
        if(m[el1][el3] > 0) map.put(m[el1][el3],0); //lewy gorny
        if(m[el1][el4] > 0) map.put(m[el1][el4],0); //prawy gorny
        if(m[el2][el3] > 0) map.put(m[el2][el3],0); //lewy dolny
        if(m[el2][el4] > 0) map.put(m[el2][el4],0); //prawy dolny


        if (m[i][j] == 0) {
            if (m[el1][j] > 0) {   //gora
                map.put(m[el1][j],map.get(m[el1][j])+1);
                count++;
            }
            if (m[el2][j] > 0) {   //dol
                map.put(m[el2][j],map.get(m[el2][j])+1);
                count++;
            }
            if (m[i][el3] > 0) {   //lewo
                map.put(m[i][el3],map.get(m[i][el3])+1);
                count++;
            }
            if (m[i][el4] > 0) {   //prawo
                map.put(m[i][el4],map.get(m[i][el4])+1);
                count++;
            }
            if (m[el1][el3] > 0) {   //lewy gorny
                map.put(m[el1][el3],map.get(m[el1][el3])+1);
                count++;
            }
            if (m[el1][el4] > 0) {   //prawy gorny
                map.put(m[el1][el4],map.get(m[el1][el4])+1);
                count++;
            }
            if (m[el2][el3] > 0) {   //lewy dolny
                map.put(m[el2][el3],map.get(m[el2][el3])+1);
                count++;
            }
            if (m[el2][el4] > 0) {   //prawy dolny
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
        int count, id, newID;
        if (m[i][j] == -1) return new int[] {9,-1};
        if (m[el1][j] != -1) list.add(m[el1][j]);
        if (m[el2][j] != -1) list.add(m[el2][j]);
        if (m[i][el3] != -1) list.add(m[i][el3]);
        if (m[i][el4] != -1) list.add(m[i][el4]);
        if (m[el1][el3] != -1) list.add(m[el1][el3]);
        if (m[el1][el4] != -1) list.add(m[el1][el4]);
        if (m[el2][el3] != -1) list.add(m[el2][el3]);
        if (m[el2][el4] != -1) list.add(m[el2][el4]);
        if (list.size() == 0) return new int[] {9,-1};
        if (isRandomElement) newID = list.get(new Random().nextInt(list.size()));
        else newID = m[i][j];
        count = (int)list.stream().filter(n -> !n.equals(newID)).count();
        id = newID;
        list.clear();
        return new int[] {count, id};
    }


}
