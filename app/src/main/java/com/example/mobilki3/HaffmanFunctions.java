package com.example.mobilki3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HaffmanFunctions {
    public static HashMap<Character, Integer> symbolCount (String text)
    {
        HashMap<Character, Integer> counter = new HashMap<Character, Integer>();

        for (int i=0; i < text.length(); i++) {
            if (!counter.containsKey(text.charAt(i)))
                counter.put(text.charAt(i), 1);
            else
                counter.put(text.charAt(i), counter.get(text.charAt(i)) + 1);
        }

        return counter;
    }

    public static HashMap<Character, Integer>
    sortByValue(HashMap<Character, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Character, Integer> > list
                = new LinkedList<Map.Entry<Character, Integer> >(
                hm.entrySet());

        // Sort the list using lambda expression
        Collections.sort(list, (i1, i2) -> i1.getValue().compareTo(i2.getValue()));

        // put data from sorted list to hashmap
        HashMap<Character, Integer> temp
                = new LinkedHashMap<Character, Integer>();
        for (Map.Entry<Character, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static ArrayList<HashMap<String, Object>> getSortedArray(ArrayList<HashMap<String, Object>> arrayList)
    {
        ArrayList<HashMap<String, Object>> sortedArrayList = new ArrayList<>();

        while (arrayList.size() > 0) {
            int min = Integer.MAX_VALUE;
            int index = 0;

            for (int i = 0; i < arrayList.size(); i++) {
                if ((int) arrayList.get(i).get("amount") < min) {
                    min = (int) arrayList.get(i).get("amount");
                    index = i;
                }
            }

            sortedArrayList.add(arrayList.get(index));
            arrayList.remove(index);
        }

        return sortedArrayList;
    }

    public static ArrayList<HashMap<String, Object>> getHaffmanTree(HashMap<Character, Integer> counter)
    {
        ArrayList<HashMap<String, Object>> tree = new ArrayList<>();

        for (Character key : counter.keySet()) {
            HashMap<String, Object> branch = new HashMap<>();
            branch.put("amount", counter.get(key));
            branch.put("symbol", key);

            tree.add(branch);
        }

        while (tree.size() > 1) {
            HashMap<String, Object> elem1 = new HashMap<>();
            HashMap<String, Object> elem2 = new HashMap<>();

            elem1 = tree.get(0);
            elem2 = tree.get(1);
            tree.remove(0);
            tree.remove(0);

            HashMap<String, Object> branch = new HashMap<>();

            if ((int) elem1.get("amount") > (int) elem2.get("amount")) {
                branch.put("amount", (int) elem1.get("amount") + (int) elem2.get("amount"));

                ArrayList<HashMap<String, Object>> subBranches = new ArrayList<>();
                subBranches.add(elem2);
                subBranches.add(elem1);

                branch.put("elms", subBranches);
            }
            else {
                branch.put("amount", (int) elem1.get("amount") + (int) elem2.get("amount"));

                ArrayList<HashMap<String, Object>> subBranches = new ArrayList<>();
                subBranches.add(elem1);
                subBranches.add(elem2);

                branch.put("elms", subBranches);
            }

            tree.add(branch);
            tree = getSortedArray(tree);
        }

        return tree;
    }

    public static HashMap<Character, String> getHaffmanCodes (String text) {
        HashMap<Character, Integer> counter = symbolCount(text);
        ArrayList<HashMap<String, Object>> tree = getHaffmanTree(sortByValue(counter));
        return getHaffmanCodes(tree.get(0));
    }
    public static HashMap<Character, String> getHaffmanCodes(HashMap<String, Object> tree) {
        HashMap<Character, String> codes = new HashMap<>();
        String way = "";
        return getHaffmanCodes(tree, codes, way);
    }
    public static HashMap<Character, String>
    getHaffmanCodes(HashMap<String, Object> tree, HashMap<Character, String> codes, String way)
    {
        if (tree.containsKey("symbol"))
            codes.put((char)tree.get("symbol"), way);
        else {
            ArrayList<HashMap<String, Object>> elems = (ArrayList<HashMap<String, Object>>) tree.get("elms");

            codes = getHaffmanCodes(elems.get(0), codes, way + "0");
            codes = getHaffmanCodes(elems.get(1), codes, way + "1");
        }

        return codes;
    }

    public static String getHashText(String text, HashMap<Character, String> codes)
    {
        StringBuilder hashText = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            hashText.append(codes.get(text.charAt(i)));
        }

        return hashText.toString();
    }

    public static Character getKeyByValue(HashMap<Character, String> codes, String bits) {
        for (char symbol : codes.keySet()) {
            if (codes.get(symbol).equals(bits)) {
                return symbol;
            }
        }
        return null;
    }

    public static String getUnHashText(String hashedText, HashMap<Character, String> codes)
    {
        StringBuilder bits = new StringBuilder();
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < hashedText.length(); i++) {
            bits.append(hashedText.charAt(i));

            if (codes.containsValue(bits.toString())) {
                text.append(getKeyByValue(codes, bits.toString()));
                bits = new StringBuilder("");
            }
        }
        return text.toString();
    }

}
