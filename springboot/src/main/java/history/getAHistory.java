package main.java.history;

import java.util.*;

import main.java.algorithm.Calculation.BayRequirement;

public class getAHistory{
    public TreeMap<String, ArrayList<ArrayList<Object>>> baseLineOccupancy;
    public TreeMap<String, ArrayList<ArrayList<Object>>> bayOccupancy;
    public int bay;
    public int minGap;
    public int maxGap;

    public getAHistory(BayRequirement br, int bay, int minGap, int maxGap){
        this.baseLineOccupancy = br.getBaseLineOccupancy();
        this.bayOccupancy = br.getBayOccupancy();
        this.bay = bay;
        this.minGap = minGap;
        this.maxGap = maxGap;
    }
}