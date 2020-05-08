package com.github.fujianlian.klinechart;

import com.github.fujianlian.klinechart.entity.IKLine;

/**
 * K线实体
 * Created by tifezh on 2016/5/16.
 */
public class KLineEntity implements IKLine {

    public String getDate() {
        return Date;
    }

    @Override
    public float getOpenPrice() {
        return Open;
    }

    @Override
    public float getHighPrice() {
        return High;
    }

    @Override
    public float getLowPrice() {
        return Low;
    }

    @Override
    public float getClosePrice() {
        return Close;
    }

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    @Override
    public float getMA30Price() {
        return MA30Price;
    }

    @Override
    public float getMA60Price() {
        return MA60Price;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getR() {
        return r;
    }

    @Override
    public float getRsi() {
        return rsi;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getVolume() {
        return Volume;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

    public void setClose(float close) {
        Close = close;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setHigh(float high) {
        High = high;
    }

    public void setLow(float low) {
        Low = low;
    }

    public void setOpen(float open) {
        Open = open;
    }

    public void setD(float d) {
        this.d = d;
    }

    public void setMA5Price(float MA5Price) {
        this.MA5Price = MA5Price;
    }

    public void setVolume(float volume) {
        Volume = volume;
    }

    public void setDea(float dea) {
        this.dea = dea;
    }

    public void setMA10Price(float MA10Price) {
        this.MA10Price = MA10Price;
    }

    public void setMA20Price(float MA20Price) {
        this.MA20Price = MA20Price;
    }

    public void setDif(float dif) {
        this.dif = dif;
    }

    public void setDn(float dn) {
        this.dn = dn;
    }

    public void setMA30Price(float MA30Price) {
        this.MA30Price = MA30Price;
    }

    public void setMA60Price(float MA60Price) {
        this.MA60Price = MA60Price;
    }

    public void setJ(float j) {
        this.j = j;
    }

    public void setK(float k) {
        this.k = k;
    }

    public void setMA5Volume(float MA5Volume) {
        this.MA5Volume = MA5Volume;
    }

    public void setMA10Volume(float MA10Volume) {
        this.MA10Volume = MA10Volume;
    }

    public void setMacd(float macd) {
        this.macd = macd;
    }

    public void setMb(float mb) {
        this.mb = mb;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setRsi(float rsi) {
        this.rsi = rsi;
    }

    public void setUp(float up) {
        this.up = up;
    }

    public String Date;
    public float Open;
    public float High;
    public float Low;
    public float Close;
    public float Volume;

    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float MA30Price;

    public float MA60Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float r;

    public float rsi;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;


}
