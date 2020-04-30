package com.kopisenja.floodmonitoring.base;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManagerNotification {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public PrefManagerNotification(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("notification", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setNotification(boolean notification){
        editor.putBoolean("notification", notification);
        editor.commit();
    }

    public boolean notification(){
        return preferences.getBoolean("notification", false);
    }
}
