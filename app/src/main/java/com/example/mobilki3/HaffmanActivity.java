package com.example.mobilki3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mobilki3.HaffmanFunctions.*;
import static com.example.mobilki3.BarGraphFunctions.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonWriter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HaffmanActivity extends AppCompatActivity {

    private EditText userText;
    private TextView answerText, codesTableText, unHashedText;
    private Button userTextHashButton, loadLastResultButton, saveUserTextButton, readFileButton, returnButton;
    private BarChart frequencyGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haffman);

        userText = findViewById(R.id.userText);
        answerText = findViewById(R.id.answerText);
        userTextHashButton = findViewById(R.id.userTextHashButton);
        frequencyGraph = (BarChart) findViewById(R.id.frequencyGraph);
        codesTableText = findViewById(R.id.codesTableText);
        unHashedText = findViewById(R.id.unHashedText);
        loadLastResultButton = findViewById(R.id.loadLastResult);
        saveUserTextButton = findViewById(R.id.userTextSaveButton);
        readFileButton = findViewById(R.id.readFileButton);
        returnButton = findViewById(R.id.returnBtn);


        // Hash Button
        userTextHashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToHash = "";

                if (!userText.getText().toString().trim().equals("")) {
                    textToHash = userText.getText().toString();

                    HashMap<Character, Integer> counter = symbolCount(textToHash);
                    HashMap<Character, String> haffmanCodes = getHaffmanCodes(textToHash);
                    String hashedText = getHashText(textToHash, haffmanCodes);
                    String UnHashedText = getUnHashText(hashedText, haffmanCodes);

                    answerText.setText(hashedText);

                    BarDataSet barDataSet = new BarDataSet(getBarEntriesFromCounter(counter), "symbols");
                    ArrayList<String> theSymbols = getBarNamesFromCounter(counter);
                    BarData symbolData = new BarData(barDataSet);
                    frequencyGraph.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theSymbols));
                    frequencyGraph.setData(symbolData);
                    frequencyGraph.setTouchEnabled(true);
                    frequencyGraph.setDrawValueAboveBar(true);
                    frequencyGraph.setDragEnabled(true);
                    frequencyGraph.setScaleEnabled(true);

                    codesTableText.setText(getCodesAsString(haffmanCodes));

                    unHashedText.setText(UnHashedText);

                    writeCodesToFile("lastResult_codes.txt", haffmanCodes);
                    writeTextToFile("lastResult_text.txt", hashedText);

                }

            }
        });

        // Save Text button
        saveUserTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = userText.getText().toString();

                if (!content.equals("")) {
                    writeTextToFile("userText.txt", content);
                } else {
                    Toast.makeText(getApplicationContext(), "Nothing to save", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Read TXT file
        readFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = readFromFile("userText.txt");

                if (!content.equals("")) {
                    userText.setText(content);
                } else {
                    Toast.makeText(getApplicationContext(), "File is empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Load Last Hash result
        loadLastResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hashedText = readFromFile("lastResult_text.txt");
                HashMap<Character, String> haffmanCodes = readHashMapFromFile("lastResult_codes.txt");

                String UnHashedText = getUnHashText(hashedText, haffmanCodes);
                HashMap<Character, Integer> counter = symbolCount(UnHashedText);

                answerText.setText(hashedText);

                BarDataSet barDataSet = new BarDataSet(getBarEntriesFromCounter(counter), "symbols");
                ArrayList<String> theSymbols = getBarNamesFromCounter(counter);
                BarData symbolData = new BarData(barDataSet);
                frequencyGraph.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theSymbols));
                frequencyGraph.setData(symbolData);
                frequencyGraph.setTouchEnabled(true);
                frequencyGraph.setDrawValueAboveBar(true);
                frequencyGraph.setDragEnabled(true);
                frequencyGraph.setScaleEnabled(true);

                codesTableText.setText(getCodesAsString(haffmanCodes));

                unHashedText.setText(UnHashedText);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity(new Intent(HaffmanActivity.this, MainActivity.class));
            }
        });
    }

    public String getCodesAsString(HashMap<Character, String> codes) {
        String codesString = "";

        for (Character elem : codes.keySet()) {
            codesString += "" + elem + ": " + codes.get(elem) + "\n";
        }

        return codesString;
    }

    public void writeCodesToFile(String fileName, HashMap<Character, String> codes) {
        File path = getApplicationContext().getFilesDir();

        try {
            File file = new File(path, fileName);
            BufferedWriter bf = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<Character, String> entry: codes.entrySet()) {
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }

            bf.flush();
            bf.close();

            Toast.makeText(getApplicationContext(), "Wrote to file: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeTextToFile(String fileName, String text) {
        File path = getApplicationContext().getFilesDir();

        try {
            FileOutputStream writer = new FileOutputStream(new File(path, fileName));
            writer.write(text.getBytes());
            writer.close();
            Toast.makeText(getApplicationContext(), "Wrote to file: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String readFromFile(String fileName) {
        File path = getApplicationContext().getFilesDir();

        try {
            File file = new File(path, fileName);
            byte[] content = new byte[(int) file.length()];

            FileInputStream stream = new FileInputStream(file);
            stream.read(content);
            return new String(content);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public HashMap<Character, String> readHashMapFromFile(String fileName) {
        File path = getApplicationContext().getFilesDir();
        HashMap<Character, String> map = new HashMap<Character, String>();

        try {
            File file = new File(path, fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");

                Character name = parts[0].toCharArray()[0];
                String value = parts[1];

                map.put(name, value);
            }

            br.close();
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<Character, String>();
        }

    }

    private void switchActivity(Intent intent) {
        try {
            finish();
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cant switch activity", Toast.LENGTH_LONG).show();
        }
    }
}
