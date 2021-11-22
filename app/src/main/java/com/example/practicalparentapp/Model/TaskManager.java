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
 * This class handles the tasks
 * that will populate the task list.
 */
public class TaskManager {
    private final ArrayList<Task> tasks;
    public static final String TASKS_FILE_NAME = "tasks.json";

    private static TaskManager instance;

    private TaskManager(Context context) { tasks = loadTasks(context); }

    public static TaskManager getInstance(Context context){
        if (instance == null) {
            instance = new TaskManager(context);
        }
        return instance;
    }

    public void add(Context context, Task task) {
        this.tasks.add(task);
        saveTasks(context, tasks);
    }

    public Task get(Context context, int index) {
        Task task = tasks.get(index);
        saveTasks(context, tasks);
        return task;
    }

    public void remove(Context context, int index) {
        tasks.remove(index);
        saveTasks(context, tasks);
    }

    public int size() {
        return tasks.size();
    }

    public static ArrayList<Task> loadTasks(Context context) {
        Gson gson = createGson();
        ArrayList<Task> tasks = null;
        File file = new File(context.getFilesDir(), TASKS_FILE_NAME);

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            tasks = gson.fromJson(bufferedReader, new TypeToken<ArrayList<Task>>(){}.getType());
            bufferedReader.close();
            fileReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found error on loading tasks file.");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("File permissions error on loading tasks file.");
            e.printStackTrace();
        }

        if (tasks == null) {
            return new ArrayList<>();
        } else {
            return tasks;
        }
    }

    public static void saveTasks(Context context, ArrayList<Task> tasks) {
        Gson gson = createGson();
        String json = gson.toJson(tasks);
        File file  = new File(context.getFilesDir(), TASKS_FILE_NAME);

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("File permissions error on writing to tasks file.");
            e.printStackTrace();
        }
    }

    public static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(
                LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }
        ).create();
    }
}
