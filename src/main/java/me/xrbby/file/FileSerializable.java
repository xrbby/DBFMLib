package me.xrbby.file;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface FileSerializable {

    @NotNull JsonFile getJsonFile();
    @NotNull String getKey();
    @NotNull JsonObject serialize();
    void deserialize(String key, JsonObject jsonObject);
}