package com.kopisenja.floodmonitoring.base;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FunctionClass {

    public static void ToastMessage(Context context, String message, int duration) {
        if (duration == 1) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static String getCurrentTime() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sfd.format(time);

        return currentTime;
    }

    public static String getCurrentDate() {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat sfd = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String currentTime = sfd.format(time);

        return currentTime;
    }
}
