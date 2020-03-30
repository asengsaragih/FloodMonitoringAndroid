package com.kopisenja.floodmonitoring.base;

import android.content.Context;
import android.widget.Toast;

import com.kopisenja.floodmonitoring.R;

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

    public String getCategoryFlood(int debit, int level) {
        if (debit < 1) {
            if (level < 2) {
                return String.valueOf(R.string.category_normal);
            } else if (level <= 3.5) {
                return String.valueOf(R.string.category_standby);
            } else {
                return String.valueOf(R.string.category_standby);
            }
        } else if (debit < 2.5) {
            if (level < 2) {
                return String.valueOf(R.string.category_normal);
            } else if (level <= 3.5) {
                return String.valueOf(R.string.category_standby);
            } else {
                return String.valueOf(R.string.category_danger);
            }
        } else {
            if (level < 2) {
                return String.valueOf(R.string.category_normal);
            } else if (level <= 3.5) {
                return String.valueOf(R.string.category_standby);
            } else {
                return String.valueOf(R.string.category_danger);
            }
        }
    }
}
