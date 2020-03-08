package ca.bcit.sunflower;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.RequiresApi;


public class Calendar_Contents {
    String date;
    int mood;
    int flow;
    Boolean period;


    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }


    public Calendar_Contents(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        Date cur_date = calendar.getTime();
        String formatted = format.format(cur_date);
        this.date = formatted;
        this.mood = getRandomInteger(2, 0);
        this.flow = getRandomInteger(2, 0);
        this.period = Boolean.FALSE;
    }


    public String getDate() {
        return this.date;
    }

    public int getMood() {
        return this.mood;
    }

    public boolean getPeriod() {
        return this.period;
    }

    public int getFlow() {
        return this.flow;
    }


}