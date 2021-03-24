package com.example.currencies;

//перевод валют
public class Converter {
    public static double Convert(double valueRub, int nominal, double course) throws Exception
    {
        if(valueRub < 0 ||nominal <= 0 || course <= 0)
            throw new Exception("unexpected parameters in Converter.Convert(...)");
        return (valueRub/course*nominal);
    }
}
