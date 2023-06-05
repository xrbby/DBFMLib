package me.xrbby.file;

import com.google.gson.JsonObject;

import java.io.File;

public record JsonFile(File file, JsonObject jsonObject) {

    public void save() { FileManager.save(this); }
}