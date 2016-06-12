package com.sam_chordas.android.stockhawk.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sudar on 12/6/16.
 * Email : hey@sudar.me
 */

public class Quote implements Parcelable {

    private String symbol;
    private String date;
    private float open;
    private float high;
    private float low;
    private float close;
    private String volume;
    private String adjClose;

    /**
     *
     * @return
     * The symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @param symbol
     * The Symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The open
     */
    public float getOpen() {
        return open;
    }

    /**
     *
     * @param open
     * The Open
     */
    public void setOpen(float open) {
        this.open = open;
    }

    /**
     *
     * @return
     * The high
     */
    public float getHigh() {
        return high;
    }

    /**
     *
     * @param high
     * The High
     */
    public void setHigh(float high) {
        this.high = high;
    }

    /**
     *
     * @return
     * The low
     */
    public float getLow() {
        return low;
    }

    /**
     *
     * @param low
     * The Low
     */
    public void setLow(float low) {
        this.low = low;
    }

    /**
     *
     * @return
     * The close
     */
    public float getClose() {
        return close;
    }

    /**
     *
     * @param close
     * The Close
     */
    public void setClose(float close) {
        this.close = close;
    }

    /**
     *
     * @return
     * The volume
     */
    public String getVolume() {
        return volume;
    }

    /**
     *
     * @param volume
     * The Volume
     */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
     *
     * @return
     * The adjClose
     */
    public String getAdjClose() {
        return adjClose;
    }

    /**
     *
     * @param adjClose
     * The Adj_Close
     */
    public void setAdjClose(String adjClose) {
        this.adjClose = adjClose;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.symbol);
        dest.writeString(this.date);
        dest.writeFloat(this.open);
        dest.writeFloat(this.high);
        dest.writeFloat(this.low);
        dest.writeFloat(this.close);
        dest.writeString(this.volume);
        dest.writeString(this.adjClose);
    }

    public Quote() {
    }

    protected Quote(Parcel in) {
        this.symbol = in.readString();
        this.date = in.readString();
        this.open = in.readFloat();
        this.high = in.readFloat();
        this.low = in.readFloat();
        this.close = in.readFloat();
        this.volume = in.readString();
        this.adjClose = in.readString();
    }

    public static final Parcelable.Creator<Quote> CREATOR = new Parcelable.Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel source) {
            return new Quote(source);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };
}