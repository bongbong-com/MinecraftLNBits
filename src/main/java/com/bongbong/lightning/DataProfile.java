package com.bongbong.lightning;

import lombok.Data;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class DataProfile {
    UUID uuid;
    String userId;
    String walletId;
    String adminKey;
    String invoiceKey;


    DataProfile importFromDocument(Document document) {
        setUuid(UUID.fromString(document.getString("uuid")));
        setUserId(document.getString("userid"));
        setWalletId(document.getString("walletid"));
        setAdminKey(document.getString("adminkey"));
        setInvoiceKey(document.getString("invoicekey"));

        return this;
    }

    Map<String, Object>  exportToDocument() {
        Map<String, Object> map = new HashMap<>();

        map.put("uuid", getUuid().toString());
        map.put("userid", getUserId());
        map.put("walletid", getWalletId());
        map.put("adminkey", getAdminKey());
        map.put("invoicekey", getInvoiceKey());

        return map;
    }
}
