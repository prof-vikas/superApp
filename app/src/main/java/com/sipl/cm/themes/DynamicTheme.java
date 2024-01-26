package com.sipl.cm.themes;

import android.content.Context;

import com.sipl.cm.R;

public class DynamicTheme {

    public static void setDynamicTheme(Context context, int selectedTheme) {

        switch (selectedTheme) {
            case 2:
                context.setTheme(R.style.Theme2);
                break;
            case 3:
                context.setTheme(R.style.Theme3);
                break;
            default:
                context.setTheme(R.style.Theme1);
        }
    }
}
