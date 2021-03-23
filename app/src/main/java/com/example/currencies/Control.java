package com.example.currencies;

import com.example.currencies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Control {
    private JSONObject currenciesJSON;
    private List<String> currenciesList;

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

    //формирует из Json object список формата (обозначение,имя,цена)
    public int makeListOfCurrencies() {
        List<String> result = new ArrayList<String>();
        JSONObject currency;

        try {
            JSONObject valute = currenciesJSON.getJSONObject("Valute");
            Iterator<String> keys = valute.keys();

            while(keys.hasNext()) {
                String key = keys.next();
                if (valute.get(key) instanceof JSONObject) {
                    currency = valute.getJSONObject(key);
                    String temp = currency.getString("CharCode")+'\n'+
                            currency.getString("Name")+'\n'+
                            currency.getDouble("Value");
                    result.add(temp);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
        currenciesList = result;
        return 0;
    }


    public List<String> getCurrenciesList() {
        return currenciesList;
    }

    public JSONObject getCurrenciesJSON() {
        return currenciesJSON;
    }
}
