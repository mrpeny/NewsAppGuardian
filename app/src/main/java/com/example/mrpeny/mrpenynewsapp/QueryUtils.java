package com.example.mrpeny.mrpenynewsapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MrPeny on 2017. 06. 12..
 */

public class QueryUtils {

    public static String fetchNewsData(String query) {
        String response = null;

        try {
            URL url = new URL(query);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream =
                        new BufferedInputStream(httpURLConnection.getInputStream());
                response = convertStreamToString(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String convertStreamToString(InputStream inputStream) {
        if (inputStream != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            try {
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();

        } else {
            return "";
        }
    }
}
