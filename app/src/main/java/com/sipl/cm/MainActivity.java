package com.sipl.cm;

import android.os.Bundle;
import android.os.Vibrator;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.sipl.cm.themes.DynamicTheme;
import com.sipl.cm.themes.ThemeManagerPreferences;
import com.sipl.cm.themes.ThemesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicTheme.setDynamicTheme(MainActivity.this, ThemeManagerPreferences.getSelectedTheme(this));
        int a = MainPreference.getSelectedTheme(MainActivity.this);
        MainPreference.setSelectedTheme(this, 2);
        if (a == 1) {
            loadFragment(new ThemesFragment(), 1);
        }
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        vibrator = (Vibrator) MainActivity.this.getSystemService(VIBRATOR_SERVICE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initialization();
    }

    private void initialization() {

        loadFragment(new ThemesFragment(), 1);
//        Button button = findViewById(R.id.Button);
//        button.setOnClickListener(view -> loadFragment(new ThemesFragment(), 1));
    }

    public void loadFragment(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag == 0) {
            ft.add(R.id.main_container, fragment);
        } else {
            ft.replace(R.id.main_container, fragment);
        }
        ft.commit();
    }


    private void getMenuNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            /*int id = item.getItemId();
            if (id == R.id.menu_item_pre_gate) {
//                loadFragmentAdmin(new QrScannerFragment(), 1, "yard");
            } else if (id == R.id.menu_item_logout) {
//                logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);*/
            return true;
        });
    }

    private void loadMenuBasedOnRoles(String userRole) {
        if (userRole.equalsIgnoreCase("")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_admin);
            getMenuNavigation();
//            loadFragment(new QrScannerFragment(), 1);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.menu_admin);
            getMenuNavigation();
//            loadFragment(new MainFragment(), 1);
        }
    }

}