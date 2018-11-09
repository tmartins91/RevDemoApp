package com.tmartins.task.revdemoapp.models;

import android.content.Context;

import java.util.Currency;

public class Rate {

    private String currencyCode;

    private Float value;

    private Float rate;

    public Rate(String currencyCode, Float rate, Float value) {
        this.currencyCode = currencyCode;
        this.value = value;
        this.rate = rate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getConvertedRate(){
        return value != null ? rate*value : 0;
    }

    public String getCurrencyName(){
        return Currency.getInstance(currencyCode).getDisplayName();
    }

    public int getFlag(Context context, String currencyCode){
        return context
                .getResources()
                .getIdentifier("flag_" + currencyCode.toLowerCase(),
                        "drawable",
                        context.getPackageName());
    }

}
