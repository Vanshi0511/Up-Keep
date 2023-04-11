package com.living.roomrental.utilities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.living.roomrental.DatePickerInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeFormatter {

    public static String getCurrentDate(){

        Date date = Calendar.getInstance().getTime();
        System.out.println("Current time => " + date);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(date);
    }

    public static void selectDateDialog(Context context , DatePickerInterface datePickerInterface) {

        Calendar calendar;
        int year,day,month;

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"-"+month+"-"+year;
                datePickerInterface.onSelectDate(date);
            }
        },year,month,day);

        dialog.show();
    }
}
