package com.sipl.cm.themes;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeManagerPreferences {

    private static final String SELECTED_THEME_KEY = "selected_theme";

    public static void setSelectedTheme(Context context, int id) {
        SharedPreferences preferences = context.getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE);
        preferences.edit().putInt(SELECTED_THEME_KEY, id).apply();
    }

    public static int getSelectedTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE);
        return preferences.getInt(SELECTED_THEME_KEY, 1);
    }
}
