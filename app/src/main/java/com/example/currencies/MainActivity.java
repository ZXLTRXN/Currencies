package com.example.currencies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {

    private EditText convertInput;
    private TextView convertRes;
    private TextView date;
    private Button convertBtn;
    private AutoCompleteTextView chooseCurrency;
    private ListView currenciesList;
    private ProgressBar loadingIndicator;

    private ArrayAdapter<String> listViewAdapter;
    private ArrayAdapter<String> autoTextViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertInput = findViewById(R.id.et_converter_input);
        convertRes = findViewById(R.id.et_converter_result);
        convertBtn = findViewById(R.id.btn_converter);
        chooseCurrency = findViewById(R.id.actv_converter);
        currenciesList = findViewById(R.id.lv_currencies);
        date = findViewById(R.id.tv_date);
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
        List<Currency> currencies = ctrl.getCurrenciesList();
        List<String> currenciesStrings = new ArrayList<>();
        List<String> currenciesCharCodes = new ArrayList<>();

        if(currencies == null) {
            Toast.makeText(getApplicationContext(),R.string.err_message_network,Toast.LENGTH_LONG).show();
        }else
        {
            for(int i=0; i < currencies.size();i++)
            {
                String tmp = currencies.get(i).toString();
                String tmp1 = currencies.get(i).getCharCode();
                currenciesStrings.add(tmp);
                currenciesCharCodes.add(tmp1);
            }
            //date.setText(ctrl.getDateOfLastUpdate().toString());
            listViewAdapter = new ArrayAdapter<String>(this, R.layout.list_item, currenciesStrings);
            currenciesList.setAdapter(listViewAdapter );

            autoTextViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, currenciesCharCodes);
            chooseCurrency.setAdapter(autoTextViewAdapter );

//            convertInput.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    int position;
//                    if((position = chooseCurrency.getListSelection())  != ListView.INVALID_POSITION)
//                    {
//                        Currency currentCur = currencies.get(position);
//                        double result = 0;
//                        try {
//                            result = Converter.Convert(Double.parseDouble(convertInput.getText().toString()),currentCur.getNominal(),currentCur.getValue());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        convertRes.setText(Double.toString(result));
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    int position;
//                    if((position = chooseCurrency.getListSelection())  != ListView.INVALID_POSITION)
//                    {
//                        Currency currentCur = currencies.get(position);
//                        double result = 0;
//                        try {
//                            result = Converter.Convert(Double.parseDouble(convertInput.getText().toString()),currentCur.getNominal(),currentCur.getValue());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        convertRes.setText(Double.toString(result));
//                    }
//
//                }
//            });

            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(parent.getAdapter() ==listViewAdapter)
                    {
                        chooseCurrency.setText(parent.getItemAtPosition(position).toString().substring(0,3));
                        chooseCurrency.dismissDropDown();
                    }
                    boolean flag = false;
                    for(Currency currency : currencies)
                    {
                        String tmp = chooseCurrency.getText().toString().toUpperCase();
                        if(tmp.equals(currency.getCharCode()))
                        {
                            double result = 0;
                            try {
                                result = Converter.Convert(Double.parseDouble(convertInput.getText().toString()),currency.getNominal(),currency.getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            convertRes.setText(String.format("%.2f",result));
                            flag = true;
                            break;
                        }
                    }

                }
            };
            chooseCurrency.setOnItemClickListener(onItemClickListener);
            currenciesList.setOnItemClickListener(onItemClickListener);

            convertBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flag = false;
                    for(Currency currency : currencies)
                    {
                        String tmp = chooseCurrency.getText().toString().toUpperCase();
                        if(tmp.equals(currency.getCharCode()))
                        {
                            double result = 0;
                            try {
                                result = Converter.Convert(Double.parseDouble(convertInput.getText().toString()),currency.getNominal(),currency.getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            convertRes.setText(String.format("%.2f",result));
                            flag = true;
                            break;
                        }
                    }
                    if(!flag) Toast.makeText(getApplicationContext(),R.string.err_message_choose,Toast.LENGTH_SHORT).show();
                }
            });

        }







    }
}