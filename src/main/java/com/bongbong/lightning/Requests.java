package com.bongbong.lightning;

import com.bongbong.lightning.responses.PayResponse;
import com.bongbong.lightning.responses.ReceiveResponse;
import com.bongbong.lightning.responses.UserManagerCreateResponse;
import com.bongbong.lightning.responses.WalletInfoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.UUID;

public class Requests {

    static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    final OkHttpClient client;
    final Gson gson;

    public Requests(OkHttpClient client) {
        this.client = client;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }


    public UserManagerCreateResponse createWallet(UUID uuid, String username) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(LightningPlugin.HOST_BASE_URL_LNBIT + "/usermanager/api/v1/users")
                .newBuilder();

        String json = "{\"admin_id\": \"" + LightningPlugin.HOST_ADMIN_KEY_LNBIT + "\", \"wallet_name\": \""
                + uuid.toString() + "\", \"user_name\": \"" + username + "\"}";

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .post(body)
                .addHeader("X-Api-Key", LightningPlugin.HOST_INVOICE_KEY_LNBIT)
                .addHeader("Content-type", "application/json; charset=utf-8")
                .build();

        Call call = client.newCall(request);

        try {
            Response response = call.execute();
            return gson.fromJson(response.body().string(), UserManagerCreateResponse.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public WalletInfoResponse getWalletBalance(String walletReadKey) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(LightningPlugin.HOST_BASE_URL_LNBIT + "/api/v1/wallet")
                .newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .addHeader("Content-type", "application/json")
                .addHeader("X-Api-Key", walletReadKey)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return gson.fromJson(response.body().string(), WalletInfoResponse.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public PayResponse payInvoice(String bolt11, String adminKey) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(LightningPlugin.HOST_BASE_URL_LNBIT + "/api/v1/payments")
                .newBuilder();

        String json = "{\"out\": true, \"bolt11\": " + bolt11 + "}";
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .post(body)
                .addHeader("Content-type", "application/json")
                .addHeader("X-Api-Key", adminKey)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return gson.fromJson(response.body().string(), PayResponse.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public ReceiveResponse createInvoice(int amount, String memo, String adminKey) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(LightningPlugin.HOST_BASE_URL_LNBIT + "/api/v1/payments")
                .newBuilder();

        String json = "{\"out\": false, \"amount\": " + amount + ", \"memo\": " + memo +"}";
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .post(body)
                .addHeader("Content-type", "application/json")
                .addHeader("X-Api-Key", adminKey)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return gson.fromJson(response.body().string(), ReceiveResponse.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

}
