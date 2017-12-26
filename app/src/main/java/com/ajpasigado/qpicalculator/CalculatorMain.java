package com.ajpasigado.qpicalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AJ Pasigado on 12/25/2017.
 */

public class CalculatorMain {
    public Map<Integer, List<Double>> lst = new HashMap<>();
    public Double totalQPI = 0.0;
    public Double totalUnits = 0.0;

    public void addData(Integer counter, Double a, Double b){
        List<Double> set = new ArrayList<Double>();
        set.add(b);
        set.add(a);
        lst.put(counter, set);
    }

    public Double getTotalQPI(){ return totalQPI; }

    public Double getTotalUnits(){ return totalUnits; }


    public void removeData(Integer a){
        lst.remove(a);
        calculateData();
    }

    public void calculateData(){
        Double total = 0.0;
        Double totalU = 0.0;
        for (Map.Entry<Integer, List<Double>> entry : lst.entrySet()) {
            List<Double> values = entry.getValue();

            totalU += values.get(0);
            total += values.get(0) * values.get(1);
        }
        totalUnits = totalU;
        totalQPI = total;
    }
}
