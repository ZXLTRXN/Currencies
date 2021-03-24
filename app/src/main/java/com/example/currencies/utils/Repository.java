package com.example.currencies.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//класс работы с файлами на устройстве
public class Repository {
    private final String fileName = "currencies";
    private final File internalStorageDir;

    public Repository(File filePath)
    {
        this.internalStorageDir = filePath;
    }


    public String readFile() throws IOException
    {
        File userStorage = new File(internalStorageDir, fileName);

        if(!userStorage.isFile()) {
            userStorage.createNewFile();
        }

        FileInputStream fis = new FileInputStream(userStorage);
        InputStreamReader isr  = new InputStreamReader(fis, "utf-8");
        BufferedReader br =new BufferedReader(isr);

        String data = "";
        data += br.readLine();


        br.close();
        isr.close();
        fis.close();
        if(data.equals("null")) return null;
        else return data;
    }

    public int writeFile(JSONObject currencies) throws IOException {
        File userStorage = new File(internalStorageDir, fileName);

        FileOutputStream fos = new FileOutputStream(userStorage);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
        osw.write(currencies.toString());

        osw.close();
        fos.close();
        return 0;
    }
}
