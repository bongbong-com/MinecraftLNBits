package com.bongbong.lightning;

import com.bongbong.lightning.database.Mongo;
import com.bongbong.lightning.database.MongoUpdate;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

import java.util.UUID;

@RequiredArgsConstructor
public class DataManager {
    final Mongo mongo;

    public DataProfile getProfile(UUID uuid) {
        Document document = mongo.getDocument("profiles", "_id",  uuid);

        if (document == null) return null;

        return new DataProfile().importFromDocument(document);
    }

    public void pushProfile(DataProfile profile) {
        MongoUpdate mu = new MongoUpdate("profiles", profile.getUuid());
        mu.setUpdate(profile.exportToDocument());

        mongo.massUpdate(mu);
    }
}
