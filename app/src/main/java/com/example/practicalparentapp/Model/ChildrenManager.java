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
 * ChildrenManager stores the parent's children.
 * Supports Singleton, to be accessed by the Coin flip
 * activity. Can add children, delete children, and edit children.
 * Supports saving in between executions with Gson.
 */
public class ChildrenManager {

    private static ChildrenManager instance;
    private static final String FILE_NAME = "childList.json";

    private ChildrenManager(Context context) {
        setChildList(context);
    }

    public static ChildrenManager getInstance(Context context) {
        if (instance == null) instance = new ChildrenManager(context);
        return instance;
    }

    private ArrayList<Child> childList = new ArrayList<>();

    public void addChildToList(Context context, Child newChild) {
        childList.add(newChild);
        saveChildList(context, childList);
    }

    public boolean isChildListEmpty() {
        return childList.isEmpty();
    }

    public Child getChild(Integer index) {
        return childList.get(index);
    }

    public boolean isChildExist(Integer index) {
        if (index >= childList.size() || index < 0) {
            return true;
        }
        return getChild(index) == null;
    }

    public ArrayList<Child> getChildList() {
        return childList;
    }

    private void setChildList(Context context) {
        childList = loadChildList(context);
    }

    public void removeChild(Context context, Integer index) {
        if (index <= childList.size()) childList.remove(getChild(index));
        saveChildList(context, childList);
    }

    //load child list from save
    public static ArrayList<Child> loadChildList(Context context) {
        //create gson parser
        Gson gson = createGson();
        ArrayList<Child> childList = null;

        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            childList = gson.fromJson(bufferedReader, new TypeToken<ArrayList<Child>>() {
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
        if (childList == null) return new ArrayList<>();
        return childList;
    }

    //loads the child list from childList.json
    public static void saveChildList(Context context, ArrayList<Child> childList) {
        //create gson parser and write list to json format
        Gson gson = createGson();
        String json = gson.toJson(childList);

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


    //create gson object to allow saving
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
