package com.example.picmah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById (R.id.botton_navigation);
        bottomNav.setOnNavigationItemSelectedListener (navListner);
        contextOfApplication = getApplicationContext();
        getSupportFragmentManager ().beginTransaction ().replace (R.id.fragment_container, new FeatureFragment ()).commit ();
    }

    // a static variable to get a reference of our application context
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener () {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId ()){
                        case R.id.nav_feature:
                            selectedFragment = new FeatureFragment ();
                            break;
                        case R.id.nav_camera:
                            selectedFragment = new CameraFragment ();
                            break;

                        case R.id.nav_Gallery:
                            selectedFragment = new GalleryFragment ();
                            break;
                    }

                    getSupportFragmentManager ().beginTransaction ().replace (R.id.fragment_container, selectedFragment).commit ();

                    return true;
                }
            };



}
