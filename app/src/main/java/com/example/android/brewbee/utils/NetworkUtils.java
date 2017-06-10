package com.example.android.brewbee.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by anish on 6/8/17.
 */

public class NetworkUtils {
    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHTPPGet(String brewSearchURL) throws IOException{
        Request request = new Request.Builder()
                .url(brewSearchURL)
                .build();
        Response response = mHTTPClient.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}
