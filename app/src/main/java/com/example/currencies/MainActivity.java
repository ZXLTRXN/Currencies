package com.example.currencies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {

    private EditText ConvertInput;
    private TextView ConvertRes;
    private Button ConvertBtn;
    private Spinner chooseCurrency;
    private ListView currenciesList;
    private ProgressBar loadingIndicator;

    private ArrayAdapter<String> listViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConvertInput = findViewById(R.id.et_converter);
        ConvertRes = findViewById(R.id.tv_converter);
        ConvertBtn = findViewById(R.id.btn_converter);
        chooseCurrency = findViewById(R.id.spinner_converter);
        currenciesList = findViewById(R.id.lv_currencies);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        loadingIndicator.setVisibility(View.VISIBLE);
        Control ctrl = new Control();

        Runnable getResp = new Runnable() {
            @Override
            public void run() {
                ctrl.updateCourse();
            }
        };
        Thread th = new Thread(getResp);
        th.start();

        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadingIndicator.setVisibility(View.INVISIBLE);
        List<String> currencies;
        if((currencies=ctrl.getCurrenciesList()) == null) {
            Toast.makeText(getApplicationContext(),R.string.err_message_network,Toast.LENGTH_LONG);
        }else
        {
            listViewAdapter = new ArrayAdapter<String>(this, R.layout.list_item, currencies);
            currenciesList.setAdapter(listViewAdapter );
        }





    }
}