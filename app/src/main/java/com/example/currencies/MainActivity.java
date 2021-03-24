package com.example.currencies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import com.example.currencies.updateServices.MyService;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {

    private EditText convertInput;
    private TextView convertRes;
    private TextView date;
    private Button convertBtn;
    private Button updateBtn;
    private AutoCompleteTextView chooseCurrency;
    private ListView currenciesList;
    private ProgressBar loadingIndicator;

    private ArrayAdapter<String> listViewAdapter;
    private ArrayAdapter<String> autoTextViewAdapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertInput = findViewById(R.id.et_converter_input);
        convertRes = findViewById(R.id.et_converter_result);
        convertBtn = findViewById(R.id.btn_converter);
        updateBtn = findViewById(R.id.btn_update_list);
        chooseCurrency = findViewById(R.id.actv_converter);
        currenciesList = findViewById(R.id.lv_currencies);
        date = findViewById(R.id.tv_date);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        loadingIndicator.setVisibility(View.VISIBLE);
        Control ctrl = new Control();

        Runnable getResp = new Runnable() {
            @Override
            public void run() {
                //читает из файла, если пуст, то с сервера
                ctrl.updateCourse(getFilesDir(),Sourse.File);
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
        // необходимые данные для активити
        List<Currency> currencies = ctrl.getCurrenciesList();
        List<String> currenciesStrings = new ArrayList<>();
        List<String> currenciesCharCodes = new ArrayList<>();

        ///обновление данных вечером, тк на сайте оно так обновляется
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.ZONE_OFFSET, 3*(60*60*1000));
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, MyService.class);
        PendingIntent pIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
        

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
            SimpleDateFormat formatForDate = new SimpleDateFormat("'релевантно на'\n dd MMM yyyy HH:mm 'местного времени \n(обновляется раз в день заранее)'");
            date.setText(formatForDate.format(ctrl.getDateOfLastUpdate()));

            //адаптеры
            listViewAdapter = new ArrayAdapter<String>(this, R.layout.list_item, currenciesStrings);
            currenciesList.setAdapter(listViewAdapter );
            autoTextViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, currenciesCharCodes);
            chooseCurrency.setAdapter(autoTextViewAdapter );

            convertInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() == 1 && s.toString().startsWith("0"))
                        s.clear();
                }
            });


            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(parent.getAdapter() ==listViewAdapter)
                    {
                        chooseCurrency.setText(parent.getItemAtPosition(position).toString().substring(0,3));
                        chooseCurrency.dismissDropDown();
                    }
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
                            if(convertInput.getText().toString().equals(""))
                                convertInput.setText("1");

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

            updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingIndicator.setVisibility(View.VISIBLE);

                    Runnable getRespFromServer = new Runnable() {
                        @Override
                        public void run() {
                            //качает с сервера
                            int res = ctrl.updateCourse(getFilesDir(), Sourse.Network);
                            if (res == 1) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(MainActivity.this, R.string.err_message_network, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else{
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(MainActivity.this, R.string.message_update, Toast.LENGTH_SHORT).show();
                                }
                            });
                            }
                        }
                    };
                    Thread th = new Thread(getRespFromServer);
                    th.start();

                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currenciesStrings.clear();
                    currenciesCharCodes.clear();

                    for(int i=0; i < currencies.size();i++)
                    {
                        String tmp = currencies.get(i).toString();
                        String tmp1 = currencies.get(i).getCharCode();
                        currenciesStrings.add(tmp);
                        currenciesCharCodes.add(tmp1);
                    }
                    listViewAdapter.notifyDataSetChanged();
                    autoTextViewAdapter.notifyDataSetChanged();
                    loadingIndicator.setVisibility(View.INVISIBLE);
                }
            });

        }

    }


}