package com.medical.parakeet.parakeet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Main_PatientDetail extends ActionBarActivity{

    View decorView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        //Puts screen in immersive sticky mode
        decorView = getWindow().getDecorView();
        stickyImmersion(decorView);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main__patient_detail);

    public void stickyImmersion(View decorView) {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
