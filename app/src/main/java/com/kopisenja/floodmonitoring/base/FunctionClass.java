package com.kopisenja.floodmonitoring.base;

import android.content.Context;
import android.widget.Toast;

public class FunctionClass {

    public static void ToastMessage(Context context, String message, int duration) {
        if (duration == 1) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
