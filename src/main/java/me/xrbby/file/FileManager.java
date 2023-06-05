package me.xrbby.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileManager {

    private Path defaultPath;

    private final ArrayList<File> registeredFiles = new ArrayList<>();
    private final ArrayList<JsonFile> jsonFiles = new ArrayList<>();

    public FileManager(Path path) {

        try {
            if(!Files.isDirectory(path))
                Files.createDirectories(path);

            this.defaultPath = path;
        } catch(Exception exception) { exception.printStackTrace(); }
    }

    private File getAdjustedFile(File file) { return new File(defaultPath.toString() + "\\" + file.getPath()); }

    public void registerFile(File file) {

        File adjustedFile = getAdjustedFile(file);

        String[] splitArray = adjustedFile.getPath().split("\\\\");

        List<String> splitList = new ArrayList<>(Arrays.asList(splitArray));

        splitList.remove(splitList.size() - 1);

        Path path = Paths.get(String.join("\\", splitList));

        try {
            if(!Files.isDirectory(path))
                Files.createDirectories(path);

            if(!adjustedFile.exists())
                adjustedFile.createNewFile();

            registeredFiles.add(adjustedFile);
        } catch(Exception exception) { exception.printStackTrace(); }
    }

    public void load() {

        for(File file : registeredFiles)
            read(file);
    }

    public JsonFile getJsonFile(File file) {

        for(JsonFile jsonFile : jsonFiles) {
            if(jsonFile.file().getName().equals(file.getName()))
                return jsonFile;
        }

        return null;
    }

    private void read(File file) {

        FileReader fileReader;

        try { fileReader = new FileReader(file); }
        catch(Exception exception) { exception.printStackTrace(); return; }

        Type type = new TypeToken<JsonObject>(){}.getType();

        JsonObject jsonObject;

        try {
            if(file.length() == 0)
                jsonObject = new Gson().fromJson("{}", type);
            else
                jsonObject = new Gson().fromJson(fileReader, type);
        } catch(Exception exception) { exception.printStackTrace(); return; }

        jsonFiles.add(new JsonFile(file, jsonObject));

        try { fileReader.close(); }
        catch(Exception exception) { exception.printStackTrace(); }
    }

    protected static void save(JsonFile jsonFile) {

        FileWriter fileWriter;

        try { fileWriter = new FileWriter(jsonFile.file()); }
        catch(Exception exception) { exception.printStackTrace(); return; }

        new Gson().toJson(jsonFile.jsonObject(), fileWriter);

        try { fileWriter.close(); }
        catch(Exception exception) { exception.printStackTrace(); }
    }
}