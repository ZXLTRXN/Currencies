package com.example.currencies.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String CB_DATA_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    public static String getResponseFromURL() throws IOException {
        URL url = null;
        String result;

        try{
            url = new URL(CB_DATA_URL);
        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            result = readString(in);
            in.close();
        }catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }finally {
            urlConnection.disconnect();
        }
        return result;
    }

    private static String readString(InputStream in) {
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");//\\A = начало строки, таким образом мы считаем построчно, по дефолту разделитель - пробел
        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }
}
