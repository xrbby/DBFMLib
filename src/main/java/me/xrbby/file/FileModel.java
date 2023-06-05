package me.xrbby.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Constructor;
import java.util.Map;

public abstract class FileModel implements FileSerializable {

    protected void save(FileOperationType operationType) {

        JsonFile jsonFile = getJsonFile();
        JsonObject jsonObject = jsonFile.jsonObject();

        switch(operationType) {
            case SAVE -> jsonObject.add(getKey(), serialize());
            case REMOVE -> jsonObject.add(getKey(), null);
        }

        jsonFile.save();
    }

    public static void load(Class<? extends FileModel> clazz, JsonFile jsonFile) throws Exception {

        JsonObject jsonObject = jsonFile.jsonObject();

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        Constructor<?> cnstr = null;

        for(Constructor<?> constructor : constructors) {
            constructor.setAccessible(true);

            if(constructor.getGenericParameterTypes().length == 0)
                cnstr = constructor;

            constructor.setAccessible(false);
        }

        if(cnstr == null)
            throw new Exception("FileModel subclasses are required to have an empty constructor");

        for(Map.Entry<String, JsonElement> entry : jsonObject.asMap().entrySet())
            ((FileModel) cnstr.newInstance()).deserialize(entry.getKey(), entry.getValue().getAsJsonObject());
    }
}