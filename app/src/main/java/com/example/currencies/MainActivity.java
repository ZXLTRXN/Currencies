package com.example.currencies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText ConvertInput;
    private TextView ConvertRes;
    private Button ConvertBtn;
    private Spinner chooseCurrency;
    private ListView CurrenciesList;
    //private ProgressBar loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConvertInput = findViewById(R.id.et_converter);
        ConvertRes = findViewById(R.id.tv_converter);
        ConvertBtn = findViewById(R.id.btn_converter);
        chooseCurrency = findViewById(R.id.spinner_converter);
        CurrenciesList = findViewById(R.id.lv_currencies);





    }
}