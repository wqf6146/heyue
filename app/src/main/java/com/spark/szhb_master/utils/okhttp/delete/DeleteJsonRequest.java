package com.spark.szhb_master.utils.okhttp.delete;


import com.spark.szhb_master.utils.okhttp.OkHttpRequest;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;


public class DeleteJsonRequest extends OkHttpRequest {

    private String body;
    private MediaType mime;

    protected DeleteJsonRequest(String url, Map params, Map<String, String> headers, String body, MediaType mime) {
        super(url, params, headers);
        this.body = body;
        this.mime = mime;
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mime, body);
    }


    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        return builder.delete(requestBody).build();
    }
}
