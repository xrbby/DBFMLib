package me.xrbby.database;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

public abstract class DatabaseModel implements DatabaseSerializable {

    private Bson getFilter() { return Filters.eq(getModelKey().keyName(), getModelKey().keyValue()); }

    protected void save(DatabaseOperationType operationType) {

        Document document = serialize();

        switch(operationType) {
            case CREATE -> getCollection().insertOne(document);
            case UPDATE -> getCollection().replaceOne(getFilter(), document);
            case REMOVE -> getCollection().deleteOne(getFilter());
        }
    }

    protected boolean read() {

        Document document = getCollection().find(getFilter()).first();

        if(document == null)
            return false;

        deserialize(document);
        return true;
    }
}