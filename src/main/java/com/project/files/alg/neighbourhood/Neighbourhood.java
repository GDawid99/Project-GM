package com.project.files.alg.neighbourhood;

import java.util.List;
import java.util.Map;

public abstract class Neighbourhood {
    protected Map<Integer,Integer> map;
    protected List<Integer> list;

    abstract public int funIf(int i, int j, int el1, int el2, int el3, int el4, int[][] m, int[][] tmpM);
    abstract public int[] mcFunIf(int i, int j, int el1, int el2, int el3, int el4, int[][] m, boolean isRandomElement);

}
