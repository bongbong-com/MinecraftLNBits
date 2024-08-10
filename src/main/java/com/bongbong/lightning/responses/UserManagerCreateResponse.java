package com.bongbong.lightning.responses;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.json.simple.JSONArray;

public @Data class UserManagerCreateResponse {
    @Expose String id, name, admin, email, password;
    @Expose JSONArray wallets;

}
