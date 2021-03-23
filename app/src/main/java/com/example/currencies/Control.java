package com.example.currencies;

import com.example.currencies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Control {
    private JSONObject currenciesJSON;
    private Date dateOfLastUpdate;
    private List<Currency> currenciesList;

    // скачивает курсы валют
    public int updateCourse(){
        String response = null;
        JSONObject temp;
//        Callable<String> getResponse = new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                String response = NetworkUtils.getResponseFromURL();
//                return response;
//            }
//        };
//        FutureTask task = new FutureTask(getResponse);
//
//        Thread t = new Thread(task);
//        t.start();
//        response = task.get();
        try {
            response = NetworkUtils.getResponseFromURL();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null)
        {
            try{
                currenciesJSON = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
            makeListOfCurrencies();
            return 0;
        }
        return 1;
    }

    //формирует из Json object список класса валют
    public int makeListOfCurrencies() {
        List<Currency> result = new ArrayList<Currency>();
        JSONObject currency;

        try {
            //dateOfLastUpdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'z").parse(currenciesJSON.getString("Date"));
            JSONObject valute = currenciesJSON.getJSONObject("Valute");
            Iterator<String> keys = valute.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                if (valute.get(key) instanceof JSONObject) {
                    currency = valute.getJSONObject(key);
                    Currency tmp = new Currency(currency.getString("CharCode"),
                            currency.getString("Name"),currency.getDouble("Value"),
                            currency.getInt("Nominal"));

                    result.add(tmp);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
        currenciesList = result;
        return 0;
    }

    public Date getDateOfLastUpdate() {
        return dateOfLastUpdate;
    }

    public List<Currency> getCurrenciesList() {
        return currenciesList;
    }

    public JSONObject getCurrenciesJSON() {
        return currenciesJSON;
    }
}
