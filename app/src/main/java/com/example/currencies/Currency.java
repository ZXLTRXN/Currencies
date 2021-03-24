package com.example.currencies;

//класс описывающий одну валюту
public class Currency {
    private String charCode;
    private String name;
    private double value;
    private int nominal;

    public Currency(String charCode, String name, double value, int nominal) {
        this.charCode = charCode;
        this.name = name;
        this.value = value;
        this.nominal = nominal;
    }

    @Override
    public String toString() {
        return   charCode + '\n' +
                name + '\n' +
                nominal + " ед. = "+
                value + " руб."
                ;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public int getNominal() {
        return nominal;
    }


}
