package com.example.mobilki3;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class BarGraphFunctions {
    public static ArrayList<BarEntry> getBarEntriesFromCounter(HashMap<Character, Integer> counter)
    {
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        int index = 0;
        for (Character elem : counter.keySet()) {
            barEntries.add(new BarEntry(index, counter.get(elem)));
            index += 1;
        }

        return barEntries;
    }

    public static ArrayList<String> getBarNamesFromCounter(HashMap<Character, Integer> counter) {
        ArrayList<String> barNames = new ArrayList<>();

        for (Character elem : counter.keySet())
            barNames.add(elem.toString());

        return barNames;
    }

}
