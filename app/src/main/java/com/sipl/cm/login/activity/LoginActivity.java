package com.sipl.cm.login.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.sipl.cm.R;
import com.sipl.cm.login.fragment.SignInFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme1);
        setContentView(R.layout.activity_login);

        loadFragment(new SignInFragment(),1);
    }

    public void loadFragment(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag == 0) {
            ft.add(R.id.main_container_login, fragment);
        } else {
            ft.replace(R.id.main_container_login, fragment);
        }
        ft.commit();
    }
}