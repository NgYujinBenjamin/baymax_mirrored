package main.java.history;

import java.util.*;

import main.java.algorithm.Calculation.BayRequirement;

public class getAHistory{
    public TreeMap<String, ArrayList<ArrayList<Object>>> baseLineOccupancy;
    public TreeMap<String, ArrayList<ArrayList<Object>>> bayOccupancy;
    public int numBays;
    public int minGap;
    public int maxGap;

    public getAHistory(BayRequirement br, int numBays, int minGap, int maxGap){
        this.baseLineOccupancy = br.getBaseLineOccupancy();
        this.bayOccupancy = br.getBayOccupancy();
        this.numBays = numBays;
        this.minGap = minGap;
        this.maxGap = maxGap;
    }
}