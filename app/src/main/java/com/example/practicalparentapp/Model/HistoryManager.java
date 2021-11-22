package com.example.practicalparentapp.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class stores the history
 * components of the Coin Flip history list.
 * Can add components and clear the list.
 * Supports saving in between executions with Gson.
 */
public class HistoryManager {

    private static HistoryManager instance;
    private static final String FILE_NAME = "historyList.json";
    private ArrayList<History> historyList = new ArrayList<>();

    private HistoryManager(Context context) {
        setHistoryList(context);
    }

    public static HistoryManager getInstance(Context context) {
        if (instance == null) instance = new HistoryManager(context);
        return instance;
    }

    public void addHistoryToList(Context context, History newHistory) {
        historyList.add(newHistory);
        saveHistoryList(context, historyList);
    }

    public ArrayList<History> getHistoryList() {
        return historyList;
    }

    private void setHistoryList(Context context) {
        historyList = loadHistoryList(context);
    }

    public static ArrayList<History> loadHistoryList(Context context) {
        //create gson parser
        Gson gson = createGson();
        ArrayList<History> historyList = null;

        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            historyList = gson.fromJson(bufferedReader, new TypeToken<ArrayList<History>>() {
            }.getType());
            bufferedReader.close();
            fileReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Reading error");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (historyList == null) return new ArrayList<>();
        return historyList;
    }

    public static void saveHistoryList(Context context, ArrayList<History> historyList) {
        //create gson parser and write list to json format
        Gson gson = createGson();
        String json = gson.toJson(historyList);

        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
    }
}
