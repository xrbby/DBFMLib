package me.xrbby.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

public interface DatabaseSerializable {

    @NotNull MongoCollection<Document> getCollection();
    @NotNull ModelKey getModelKey();
    @NotNull Document serialize();
    void deserialize(Document document);
}