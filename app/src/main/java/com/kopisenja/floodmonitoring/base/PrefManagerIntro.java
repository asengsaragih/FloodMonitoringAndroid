package com.kopisenja.floodmonitoring.base;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManagerIntro {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public PrefManagerIntro(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("intro", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setIntro(boolean loggedin){
        editor.putBoolean("intro", loggedin);
        editor.commit();
    }

    public boolean intro(){
        return preferences.getBoolean("intro", false);
    }
}
