package com.example.currencies.updateServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.currencies.Control;
import com.example.currencies.Sourse;

public class MyService extends Service {

    private Control ctrl;

    //сервис обновления данных в определенное время
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctrl = new Control();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable getResp = new Runnable() {
            @Override
            public void run() {
                //читает с сервера
                ctrl.updateCourse(getFilesDir(),Sourse.Network);
            }
        };
        Thread th = new Thread(getResp);
        th.start();

        stopService(new Intent(this,MyService.class));
        return START_NOT_STICKY;
    }
}