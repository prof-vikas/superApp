package com.sipl.cm;

import android.content.Context;
import android.content.SharedPreferences;

public class MainPreference {

    public static final String SELECTED_FRAGMENT = "selected_fragment";

    public static void setSelectedTheme(Context context, int id) {
        SharedPreferences preferences = context.getSharedPreferences("theme_fragment", Context.MODE_PRIVATE);
        preferences.edit().putInt(SELECTED_FRAGMENT, id).apply();
    }

    public static int getSelectedTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("theme_fragment", Context.MODE_PRIVATE);
        return preferences.getInt(SELECTED_FRAGMENT, 2);
    }
}
