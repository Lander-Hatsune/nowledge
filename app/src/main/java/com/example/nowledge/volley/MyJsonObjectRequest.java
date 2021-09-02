package com.example.nowledge.volley;

import android.location.GnssAntennaInfo;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyJsonObjectRequest extends JsonObjectRequest {
    public MyJsonObjectRequest(int method, String url, @Nullable JSONObject params, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, params, listener, errorListener);
    }

    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=utf-8";
    }
}
