package com.sipl.cm.themes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.sipl.cm.MainActivity;
import com.sipl.cm.MainPreference;
import com.sipl.cm.R;

public class ThemesFragment extends Fragment {

    private static final String TAG = "ThemesFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicTheme.setDynamicTheme(requireContext(), ThemeManagerPreferences.getSelectedTheme(requireContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_themes, container, false);

        initialization(view);
        return view;
    }

    private void initialization(View view) {
        ImageButton btnMac = view.findViewById(R.id.img_btn_theme_mac);
        ImageButton btnGreen = view.findViewById(R.id.img_btn_theme_green);
        ImageButton btnRed = view.findViewById(R.id.img_btn_theme_red);

        btnMac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeManagerPreferences.setSelectedTheme(requireContext(), 3);
                recreateThemeFragment();
            }
        });

        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeManagerPreferences.setSelectedTheme(requireContext(), 2);
                recreateThemeFragment();
            }
        });

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeManagerPreferences.setSelectedTheme(requireContext(), 1);
                recreateThemeFragment();
            }
        });


    }

    private void recreateThemeFragment() {
        MainPreference.setSelectedTheme(requireContext(), 1);
        requireActivity().startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finish();
    }
}