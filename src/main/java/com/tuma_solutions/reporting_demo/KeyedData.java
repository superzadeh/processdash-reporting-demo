package com.tuma_solutions.reporting_demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class KeyedData {
    
    private SortedSet<String> keyNames;
    
    private Map<String, Double> data;

    public KeyedData(SortedSet<String> keyNames) {
        this.keyNames = keyNames;
        this.data = new HashMap<String, Double>();
    }

    public void putData(String key, double value) {
        keyNames.add(key);
        data.put(key, value);
    }
    
    public double getData(String key) {
        Double result = data.get(key);
        return (result == null ? 0 : result);
    }

    public List<Double> asList() {
        List result = new ArrayList();
        for (String oneKey : keyNames) {
            Double val = data.get(oneKey);
            if (val == null)
                val = 0.0;
            result.add(val);
        }
        return result;
    }

    public double sum() {
        double result = 0;
        for (Double d : data.values())
            result += d;
        return result;
    }

}
